package com.example.telegram.ui.message_recyler_view.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.R
import com.example.telegram.database.CURRENT_UID
import com.example.telegram.ui.message_recyler_view.views.MessageView
import com.example.telegram.utilits.asTime
import com.example.telegram.utilits.photoDownloadAndSet

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blockUserImageMessage: ConstraintLayout =
        view.findViewById(R.id.block_user_image_message)
    private val chatUserImage: ImageView = view.findViewById(R.id.chat_user_image)
    private val chatUserImageMessageTime: TextView =
        view.findViewById(R.id.chat_user_image_message_time)
    private val blockReceivedImageMessage: ConstraintLayout =
        view.findViewById(R.id.block_received_image_message)
    private val chatReceivedImage: ImageView = view.findViewById(R.id.chat_received_image)
    private val chatReceivedImageMessageTime: TextView =
        view.findViewById(R.id.chat_received_image_message_time)

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockUserImageMessage.visibility = View.VISIBLE
            blockReceivedImageMessage.visibility = View.GONE
            chatUserImage.photoDownloadAndSet(view.fileUrl)
            chatUserImageMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blockUserImageMessage.visibility = View.GONE
            blockReceivedImageMessage.visibility = View.VISIBLE
            chatReceivedImage.photoDownloadAndSet(view.fileUrl)
            chatReceivedImageMessageTime.text =
                view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
//        TODO("Not yet implemented")
    }

    override fun onDetach() {
//        TODO("Not yet implemented")
    }
}