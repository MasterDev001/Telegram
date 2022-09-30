package com.example.telegram.utilits

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.OpenableColumns
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.telegram.MainActivity
import com.example.telegram.R
import com.example.telegram.database.updatePhonesToDatabase
import com.example.telegram.model.CommonModel
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message: String) {          ////  bu funksiya faqat fragmentda ishlaydi
    Toast.makeText(APP_ACTIVITIY, message, Toast.LENGTH_SHORT).show()
}

fun restartActivity() {
    val intent = Intent(APP_ACTIVITIY, MainActivity::class.java)
    APP_ACTIVITIY.startActivity(intent)
    APP_ACTIVITIY.finish()
}

fun replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        APP_ACTIVITIY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.data_container, fragment)
            .commit()
    } else {
        APP_ACTIVITIY.supportFragmentManager.beginTransaction()
            .replace(R.id.data_container, fragment)
            .commit()
    }
}

fun hideKeyboard() {
    val imm: InputMethodManager =
        APP_ACTIVITIY.getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager//
    //Activitiyni servisini olyabdi
    imm.hideSoftInputFromWindow(APP_ACTIVITIY.window.decorView.windowToken, 0)
}

fun ImageView.photoDownloadAndSet(url: String) {
    Glide.with(this)
        .load(url)
        .fitCenter()
        .centerCrop()
        .placeholder(R.drawable.ic_profile)// rasm yuklangincha o'rniga chiqadigon rasm
        .into(this)
}

@SuppressLint("Range")
fun initContacts() {                                                        // // / // / //
    if (checkPermission(READ_CONTACTS)) {
        val arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITIY.contentResolver.query(//activityni pakage adresini olyabdi
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,//telefon adreesini olyabdi
            null,
            null,
            null,
            null
        )
        cursor?.let {    //for eachga o'xsahsh
            if (it.moveToNext()) {//keyingisi mavjud bo'lsa
                val fullName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                //getcolumnindex qatorni indexni qaytaradi
                val phone =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullName
                newModel.phone = phone.replace(Regex("[\\s,-]"), "")
                arrayContacts.add(newModel)
            }
        }
        cursor?.close()
        updatePhonesToDatabase(arrayContacts)
    }
}

fun String.asTime(): String {                                                   ///// / // / / / /
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

@SuppressLint("Range")
fun getFileNameFromUri(uri: Uri?): String {                                            // / / / / /
    var result = ""
    val cursor = APP_ACTIVITIY.contentResolver.query(uri!!, null, null, null, null)// ////
    try {
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    } catch (e: Exception) {
        showToast(e.message.toString())
    } finally {
        cursor?.close()
        return result
    }
}

fun getPlurals(count: Int) =
                                                 //quantity = miqdor
    APP_ACTIVITIY.resources.getQuantityString(R.plurals.count_members, count, count)      // / // //