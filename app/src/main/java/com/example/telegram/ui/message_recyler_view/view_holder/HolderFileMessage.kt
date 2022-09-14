package com.example.telegram.ui.message_recyler_view.view_holder

import android.annotation.SuppressLint
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.R
import com.example.telegram.database.CURRENT_UID
import com.example.telegram.database.getFileFromStorage
import com.example.telegram.ui.message_recyler_view.views.MessageView
import com.example.telegram.utilits.WRITE_FILES
import com.example.telegram.utilits.asTime
import com.example.telegram.utilits.checkPermission
import com.example.telegram.utilits.showToast
import java.io.File
import java.lang.Exception

class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blockUserFileMessage: ConstraintLayout =
        view.findViewById(R.id.block_user_file_message)
    private val chatUserFileMessageTime: TextView =
        view.findViewById(R.id.chat_user_file_message_time)
    private val blockReceivedFileMessage: ConstraintLayout =
        view.findViewById(R.id.block_received_file_message)
    private val chatReceivedFileMessageTime: TextView =
        view.findViewById(R.id.chat_received_file_message_time)

    private val chatUserFilename: TextView = view.findViewById(R.id.chat_user_filename)
    private val chatUserBtnDownload: ImageView = view.findViewById(R.id.chat_user_btn_download)
    private val chatUserProgressBar: ProgressBar = view.findViewById(R.id.chat_user_progress_bar)

    private val chatReceivedFilename: TextView = view.findViewById(R.id.chat_received_filename)
    private val chatReceivedBtnDownload: ImageView =
        view.findViewById(R.id.chat_received_btn_download)
    private val chatReceivedProgressBar: ProgressBar =
        view.findViewById(R.id.chat_received_progress_bar)

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockUserFileMessage.visibility = View.VISIBLE
            blockReceivedFileMessage.visibility = View.GONE
            chatUserFileMessageTime.text =
                view.timeStamp.asTime()
            chatUserFilename.text = view.text
        } else {
            blockUserFileMessage.visibility = View.GONE
            blockReceivedFileMessage.visibility = View.VISIBLE
            chatReceivedFileMessageTime.text =
                view.timeStamp.asTime()
            chatReceivedFilename.text = view.text
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == CURRENT_UID)
            chatUserBtnDownload.setOnClickListener { clickToBtnFile(view) }
        else chatReceivedBtnDownload.setOnClickListener { clickToBtnFile(view) }
    }

    private fun clickToBtnFile(view: MessageView) {
        if (view.from == CURRENT_UID) {
            chatUserBtnDownload.visibility = View.INVISIBLE
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            chatReceivedBtnDownload.visibility = View.INVISIBLE
            chatReceivedProgressBar.visibility = View.VISIBLE
        }
        val file =
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),//download packageni olyabturgang ao'xshaydi
                view.text
            )
        Log.d("TAG", "click: ${file}")
        try {
//            if (checkPermission(WRITE_FILES)) {
                file.createNewFile()
                Log.d("TAG", "click: $file  ,${view.fileUrl} ")
                getFileFromStorage(file, view.fileUrl) {                         //  / / /// // / /
                    if (view.from == CURRENT_UID) {
                        Log.d("TAG", "clickToBtnFile: $file  ,${view.fileUrl} ")
                        chatUserBtnDownload.visibility = View.VISIBLE
                        chatUserProgressBar.visibility = View.INVISIBLE
                    } else {
                        chatReceivedBtnDownload.visibility = View.VISIBLE
                        chatReceivedProgressBar.visibility = View.INVISIBLE

                }
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    override fun onDetach() {
        chatUserBtnDownload.setOnClickListener(null)
        chatReceivedBtnDownload.setOnClickListener(null)
    }
}