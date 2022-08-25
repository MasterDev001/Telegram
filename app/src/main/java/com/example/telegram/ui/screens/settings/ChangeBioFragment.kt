package com.example.telegram.ui.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.telegram.R
import com.example.telegram.database.USER
import com.example.telegram.database.setBioToDatabase
import com.example.telegram.databinding.FragmentChangeBioBinding
import com.example.telegram.ui.screens.base.BaseChangeFragment

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    private lateinit var binding: FragmentChangeBioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeBioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.settingsInputBio.setText(USER.bio)
    }

    override fun change() {
        super.change()

        val newBio = binding.settingsInputBio.text.toString()
        setBioToDatabase(newBio)
    }
}