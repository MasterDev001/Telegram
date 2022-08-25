package com.example.telegram.ui.screens.base

import android.view.*
import androidx.fragment.app.Fragment
import com.example.telegram.R
import com.example.telegram.utilits.APP_ACTIVITIY
import com.example.telegram.utilits.hideKeyboard

open class BaseChangeFragment(layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        APP_ACTIVITIY.mAppDrawer.disableDrawer()
        hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITIY.mAppDrawer.enableDrawer()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        APP_ACTIVITIY.menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    open fun change() {

    }

}