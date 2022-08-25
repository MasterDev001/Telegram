package com.example.telegram.ui.screens.settings

import android.os.Bundle
import android.view.*
import com.example.telegram.R
import com.example.telegram.database.USER
import com.example.telegram.database.setNameToDatabase
import com.example.telegram.databinding.FragmentChangeNameBinding
import com.example.telegram.ui.screens.base.BaseChangeFragment
import com.example.telegram.utilits.*

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    private lateinit var binding: FragmentChangeNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val fullNameList = USER.fullname.split(" ")
        if (fullNameList.size > 1) {
            binding.settingsInputName.setText(fullNameList[0])
            binding.settingsInputSurname.setText(fullNameList[1])
        } else binding.settingsInputName.setText(fullNameList[0])
    }

    override fun change() {
        val name = binding.settingsInputName.text.toString()
        val surName = binding.settingsInputSurname.text.toString()
        if (name.isEmpty()) {
            showToast("Name is empty")
        } else {
            val fullname = "$name $surName"
            setNameToDatabase(fullname)
        }
    }
}