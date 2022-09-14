package com.example.telegram

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.telegram.database.AUTH
import com.example.telegram.database.initFirebase
import com.example.telegram.database.initUser
import com.example.telegram.databinding.ActivityMainBinding
import com.example.telegram.ui.screens.main_list.MainListFragment
import com.example.telegram.ui.screens.register.EnterPhoneNumberFragment
import com.example.telegram.ui.objects.AppDrawer
import com.example.telegram.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        APP_ACTIVITIY = this
        initFirebase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch { //corotino ichidagilar ishlasaxam app qotirmaydi
                initContacts()
            }
            initFields()
            initFunc()
        }
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)//action barni toolbarga o'zgartiryapti
        if (AUTH.currentUser != null) {
            mAppDrawer.create()
            replaceFragment(MainListFragment(), false)
        } else {
            replaceFragment(EnterPhoneNumberFragment(), false)
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolBar
        mAppDrawer = AppDrawer()
    }

    override fun onStart() {
        super.onStart()
        AppState.updateState(AppState.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppState.updateState(AppState.OFFLINE)
    }
                                                                                        ///// / / / /
    override fun onRequestPermissionsResult( //adashmasam requsest perrmissinog ruxsat soredi =
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                APP_ACTIVITIY,
                READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED // agar read contacts permissioni berilgan bo'lsa initcontacts() ishlidid
        ) {
            initContacts()
        }
    }
}