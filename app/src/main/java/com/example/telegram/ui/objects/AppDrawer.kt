package com.example.telegram.ui.objects

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.*
import com.example.telegram.R
import com.example.telegram.database.USER
import com.example.telegram.ui.screens.contacts.ContactsFragment
import com.example.telegram.ui.screens.groups.AddContactsFragment
import com.example.telegram.ui.screens.settings.SettingsFragment
import com.example.telegram.utilits.*
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

class AppDrawer {

    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mCurrentProfile: ProfileDrawerItem

    fun create() {
        initLoader()
        createHeader()
        createDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    fun disableDrawer() {
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false//menuni 3 talik iconini ko'rsatadi
        APP_ACTIVITIY.supportActionBar?.setDisplayHomeAsUpEnabled(true)//nazad strelkasini yoqadi
        mDrawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)//yondan tortisa chiqishiini false qolyabdi
        APP_ACTIVITIY.mToolbar.setNavigationOnClickListener {
            APP_ACTIVITIY.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer() {
        APP_ACTIVITIY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        mDrawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
        APP_ACTIVITIY.mToolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }

    private fun createDrawer() {//toolbardagi chap tomon menu(Drawer) yaratadi
        mDrawer = DrawerBuilder()
            .withActivity(APP_ACTIVITIY)
            .withAccountHeader(mHeader)//boshligi
            .withActionBarDrawerToggle(true)//qisqartirib uzaytirish
            .withSelectedItem(-1)//default tanlangan item id si berilyabdi
            .withToolbar(APP_ACTIVITIY.mToolbar)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(100)
                    .withName("Create Group")
                    .withSelectable(false)
                    .withIconTintingEnabled(true)//rang berish
                    .withIcon(R.drawable.ic_menu_create_groups),
                PrimaryDrawerItem().withIdentifier(101)
                    .withName("Create secret chat")
                    .withSelectable(false)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_outline_lock_24),
                PrimaryDrawerItem().withIdentifier(102)
                    .withName("Create Channel")
                    .withSelectable(false)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_megaphone_speaker_svgrepo_com),
                PrimaryDrawerItem().withIdentifier(103)
                    .withName("Contacts")
                    .withSelectable(false)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_contacts),
                PrimaryDrawerItem().withIdentifier(104)
                    .withName("Calls")
                    .withSelectable(false)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_phone),
                PrimaryDrawerItem().withIdentifier(105)
                    .withName("Saved Messages")
                    .withSelectable(false)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_favorites),
                PrimaryDrawerItem().withIdentifier(106)
                    .withName("Settings")
                    .withSelectable(false)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_settings),
                DividerDrawerItem(),// chiziq qo'yib, bo'ladi
                PrimaryDrawerItem().withIdentifier(107)
                    .withName("Invite Friends")
                    .withSelectable(false)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_invate),
                PrimaryDrawerItem().withIdentifier(108)
                    .withName("Telegram Features")
                    .withSelectable(false)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_menu_help)
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                //drawer knopkalari boasiladi
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    clickToItem(position)
                    return false
                }
            }).build()
    }

    private fun clickToItem(position: Int) {
        when (position) {
            1 -> replaceFragment(AddContactsFragment())
            7 -> replaceFragment(SettingsFragment())
            4 -> replaceFragment(ContactsFragment())
        }
    }

    private fun createHeader() {
        mCurrentProfile = ProfileDrawerItem()
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
            .withIdentifier(200)
        mHeader = AccountHeaderBuilder()
            .withActivity(APP_ACTIVITIY)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(mCurrentProfile).build()
        //Header yasaydi
    }

    fun updateHeader() {
        mCurrentProfile
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)

        mHeader.updateProfile(mCurrentProfile)
    }

    private fun initLoader() {//o'zni funksiyasi loaderlarni init qiolyabdi
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                imageView.photoDownloadAndSet(uri.toString())
            }
        })
    }
}