package com.example.telegram.utilits

import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class AppVoiceRecorder {

    private var mMediaRecorder = MediaRecorder()
    private lateinit var mFile: File
    private lateinit var mMessageKey: String

    fun startRecorder(messageKey: String) {
        try {
            mMessageKey = messageKey
            createFileRecorder()
            prepareMediaRecorder()
            mMediaRecorder.start()
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    private fun prepareMediaRecorder() {
        mMediaRecorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            setOutputFile(mFile.absolutePath)                                //// //  //
            prepare()
        }
/*/            mMediaRecorder.reset()
//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
//            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
//            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
//            mMediaRecorder.setOutputFile(mFile.absolutePath)                                //// //  //
*///          mMediaRecorder.prepare()
    }

    private fun createFileRecorder() {
        mFile = File(
            APP_ACTIVITIY.filesDir,  //kelyapturgan app contextni file papkasini qaytaradi
            mMessageKey    ///va unga message key nomi bilan file ochadi file creat bo'lganda
        )                                  /// / / // /
        mFile.createNewFile()
    }

    fun stopRecorder(onSuccess: (file: File, messageKey: String) -> Unit) {                 //// / / //
        try {
            mMediaRecorder.stop()
            onSuccess(mFile, mMessageKey)
        } catch (e: Exception) {
            showToast(e.message.toString())
            mFile.delete()
        }
    }

    fun releaseRecorder() {
        try {
            mMediaRecorder.release()
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }
}