package com.example.telegram.ui.screens.main_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.R
import com.example.telegram.database.*
import com.example.telegram.databinding.FragmentMainListBinding
import com.example.telegram.model.CommonModel
import com.example.telegram.utilits.*


class MainListFragment : Fragment(R.layout.fragment_main_list) {

    private lateinit var mBinding: FragmentMainListBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MainListAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMainListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITIY.title = "Telegram"
        APP_ACTIVITIY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
    }

    private fun initRecyclerView() {                                             // / // / // / //
        mRecyclerView = mBinding.mainListRecyclerView
        mAdapter = MainListAdapter()

        ////season 1
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { commonModel ->

                when (commonModel.type) {
                    TYPE_CHAT -> showChat(commonModel)
                    TYPE_GROUP -> showGroup(commonModel)
                }
            }
        })
        mRecyclerView.adapter = mAdapter
    }

    private fun showGroup(commonModel: CommonModel) {
        ////season 2
        REF_DATABASE_ROOT.child(NODE_GROUPS).child(commonModel.id)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                val newModel = dataSnapshot1.getCommonModel()

                ////season 3
                REF_DATABASE_ROOT.child(NODE_GROUPS).child(commonModel.id).child(NODE_MESSAGES)
                    .limitToLast(1)            // // // /
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                        val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                        if (tempList.isEmpty()) {
                            newModel.lastMessage = "History Cleanedmain_list_item.xml"
                        } else {
                            newModel.lastMessage = tempList[0].text
                        }
                        newModel.type = TYPE_GROUP
                        mAdapter.updateListItems(newModel)
                    })
            })
    }

    private fun showChat(commonModel: CommonModel) {
        ////season 2
        mRefUsers.child(commonModel.id)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                val newModel = dataSnapshot1.getCommonModel()

                ////season 3
                mRefMessages.child(commonModel.id).limitToLast(1)            // // // /
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                        val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                        if (tempList.isEmpty()) {
                            newModel.lastMessage = "History Cleanedmain_list_item.xml"
                        } else {
                            newModel.lastMessage = tempList[0].text
                        }

                        if (newModel.fullname.isEmpty())
                            newModel.fullname = newModel.phone
                        newModel.type = TYPE_CHAT
                        mAdapter.updateListItems(newModel)
                    })
            })
    }

}