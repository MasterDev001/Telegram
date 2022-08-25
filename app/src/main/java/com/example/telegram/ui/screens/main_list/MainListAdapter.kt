package com.example.telegram.ui.screens.main_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.R
import com.example.telegram.model.CommonModel
import com.example.telegram.ui.screens.groups.GroupChatFragment
import com.example.telegram.ui.screens.single_chat.SingleChatFragment
import com.example.telegram.utilits.TYPE_CHAT
import com.example.telegram.utilits.TYPE_GROUP
import com.example.telegram.utilits.photoDownloadAndSet
import com.example.telegram.utilits.replaceFragment
import de.hdodenhof.circleimageview.CircleImageView

class MainListAdapter : RecyclerView.Adapter<MainListAdapter.MainListHolder>() {

    private var listItems = mutableListOf<CommonModel>()

    class MainListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.main_list_item_name)
        val itemLastMessage: TextView = view.findViewById(R.id.main_list_last_message)
        val itemPhoto: CircleImageView = view.findViewById(R.id.main_list_item_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        // o'zini layoutini qaytarishi kere edi lekn itemview clik bo'lganda boshqa fragmentga almashtiryabdi
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        val holder = MainListHolder(view)
        holder.itemView.setOnClickListener {
            when (listItems[holder.adapterPosition].type) {
                TYPE_CHAT -> replaceFragment(SingleChatFragment(listItems[holder.adapterPosition]))
                TYPE_GROUP -> replaceFragment(GroupChatFragment(listItems[holder.adapterPosition]))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.photoDownloadAndSet(listItems[position].photoUrl)
    }

    override fun getItemCount() = listItems.size

    fun updateListItems(item: CommonModel) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}