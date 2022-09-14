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
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT) //jo'natish formati
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT) //audioni encodi formatga o'xshash
            setOutputFile(mFile.absolutePath) // chiqadigon file ni set qilyabdi mfile ni absolute adressiga
            prepare()
        }
/*/            mMediaRecorder.reset()
//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
//            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
//            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
//            mMediaRecorder.setOutputFile(mFile.absolutePath)
*///          mMediaRecorder.prepare()
    }

    private fun createFileRecorder() {
        mFile = File(
            APP_ACTIVITIY.filesDir,  //kelyapturgan app contextni file papkasini qaytaradi
            mMessageKey    ///va unga message key nomi bilan file ochadi file creat bo'lganda
        )
        mFile.createNewFile()
    }

    fun stopRecorder(onSuccess: (file: File, messageKey: String) -> Unit) {
        try {
            mMediaRecorder.stop()
            onSuccess(
                mFile,
                mMessageKey
            )// m file bilan mMessageKey ni stoprecorder ishlatiladigon joyga jo'natyabdi
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