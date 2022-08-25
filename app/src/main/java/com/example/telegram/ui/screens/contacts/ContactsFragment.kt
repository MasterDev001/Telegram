package com.example.telegram.ui.screens.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.R
import com.example.telegram.database.*
import com.example.telegram.databinding.FragmentContactsBinding
import com.example.telegram.model.CommonModel
import com.example.telegram.ui.screens.base.BaseFragment
import com.example.telegram.ui.screens.single_chat.SingleChatFragment
import com.example.telegram.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefUsersListener: AppValueEventListener
    private var mapListeners = hashMapOf<DatabaseReference, AppValueEventListener>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        APP_ACTIVITIY.title = "Contacts"
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = binding.contactsRecyclerView
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java).build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                return ContactsHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.contact_item, parent, false)
                )
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                mRefUsersListener = AppValueEventListener {    /// // / // / /
                    val contact = it.getCommonModel()

                    if (contact.fullname.isEmpty()){
                    holder.name.text = model.fullname}
                    else { holder.name.text = contact.fullname}

                    holder.status.text = contact.state
                    holder.photo.photoDownloadAndSet(contact.photoUrl)
                    holder.itemView.setOnClickListener { replaceFragment(SingleChatFragment(model)) }
                }
                mRefUsers.addListenerForSingleValueEvent(mRefUsersListener)
                mapListeners[mRefUsers] = mRefUsersListener         //  . ... . / /
            }
        }
        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.contact_fullname)
        val status: TextView = view.findViewById(R.id.contacts_status)
        val photo: CircleImageView = view.findViewById(R.id.contact_photo)
    }

    override fun onStop() {
        super.onStop()

        mAdapter.stopListening()
        mapListeners.forEach {
            it.key.removeEventListener(it.value)
        }
    }
}


