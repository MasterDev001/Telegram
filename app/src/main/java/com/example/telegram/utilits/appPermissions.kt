package com.example.telegram.utilits

import android.content.pm.PackageManager
import android.os.Build
import android.webkit.PermissionRequest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

const val READ_CONTACTS = android.Manifest.permission.READ_CONTACTS
const val RECORD_AUDIO = android.Manifest.permission.RECORD_AUDIO
const val WRITE_FILES = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

const val PERMISSION_REQUEST = 200


fun checkPermission(permission: String): Boolean {    //////
    return if (Build.VERSION.SDK_INT >= 23
        && ContextCompat.checkSelfPermission(
            APP_ACTIVITIY,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITIY, arrayOf(permission), PERMISSION_REQUEST)
        false
    } else true

}