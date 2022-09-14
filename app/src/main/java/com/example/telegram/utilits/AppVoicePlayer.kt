package com.example.telegram.utilits

import android.media.MediaPlayer
import com.example.telegram.database.getFileFromStorage
import java.io.File
import java.lang.Exception

class AppVoicePlayer {

    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mFile: File

    fun play(messageKey: String, fileUrl: String, function: () -> Unit) {

        mFile = File(APP_ACTIVITIY.filesDir, messageKey)  /// fileni olyabdi app activity directoriyasidjylashgan message key nomli file ni
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile) { //file mavjudligi va lengz >0 va file ligi catologemasligini tekshiryabdi
            startPlay {
                function()
            }
        } else {
            mFile.createNewFile()
            getFileFromStorage(mFile, fileUrl) {                                  // / //
                startPlay {
                    function()
                }
            }
        }
    }

    private fun startPlay(function: () -> Unit) {
        try {
            mMediaPlayer.setDataSource(mFile.absolutePath) //mfileni absolut pathini oladi yani mutloq yo'lini
            mMediaPlayer.prepare()      //tayyorlayabdi
            mMediaPlayer.start()
            mMediaPlayer.setOnCompletionListener {   //complete bolganda(tugaganda) ishlaydi
                stop {
                    function()
                }
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    fun stop(function: () -> Unit) {
        try {
            mMediaPlayer.stop()
            mMediaPlayer.reset()
            function()
        } catch (e: Exception) {
            showToast(e.message.toString())
            function()
        }
    }

    fun release() {
        mMediaPlayer.release()
    }

    fun init() {
        mMediaPlayer = MediaPlayer()
    }
}