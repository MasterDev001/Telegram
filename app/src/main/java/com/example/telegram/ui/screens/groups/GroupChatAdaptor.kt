package com.example.telegram.ui.screens.groups

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.ui.message_recyler_view.view_holder.*
import com.example.telegram.ui.message_recyler_view.views.MessageView

class GroupChatAdaptor : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessageCache = mutableListOf<MessageView>()
    private var mListHolders= mutableListOf<MessageHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolder).drawMessage(mListMessageCache[position])
    }

    override fun getItemViewType(position: Int): Int {
        return mListMessageCache[position].getTypeView()
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {//view oynaga biriktirilganda ichi ishledi
        // Called when a view created by this adapter has been attached (biriktirilgan) to a window.
        (holder as MessageHolder).onAttach(mListMessageCache[holder.adapterPosition])   //// / / //
        mListHolders.add( (holder as MessageHolder))
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {//view oynadan ajraganda ishledi
        // Called when a view created by this adapter has been detached from its window.
        // Ushbu adapter tomonidan yaratilgan view uning oynasidan ajratilganda (detached) chaqiriladi.
        (holder as MessageHolder).onDetach()                                          /// // / //
        mListHolders.remove((holder as MessageHolder))
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount() = mListMessageCache.size

    fun addItemToBottom(item: MessageView, function: () -> Unit) {//sms yangi bo'lsa qo'shadi
        if (!mListMessageCache.contains(item)) {
            mListMessageCache.add(item)
            notifyItemInserted(mListMessageCache.size)
        }
        function()
    }

    fun addItemToTop(item: MessageView, function: () -> Unit) {//
        if (mListMessageCache.contains(item)) {
            mListMessageCache.add(item)
            mListMessageCache.sortBy { it.timeStamp }
            notifyItemInserted(0)
        }
        function()
    }
    fun onDestroy(){
        mListHolders.forEach {
            it.onDetach()
        }
    }
}
