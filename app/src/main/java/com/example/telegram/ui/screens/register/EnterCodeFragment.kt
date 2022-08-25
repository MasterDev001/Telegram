package com.example.telegram.ui.screens.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.telegram.R
import com.example.telegram.database.*
import com.example.telegram.databinding.FragmentEnterCodeBinding
import com.example.telegram.utilits.*
import com.google.firebase.auth.PhoneAuthProvider

class EnterCodeFragment(val phoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    private lateinit var binding: FragmentEnterCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        APP_ACTIVITIY.title = phoneNumber

        binding.registerInputCode.addTextChangedListener(AppTextWatcher {  ////
            val string = binding.registerInputCode.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    fun enterCode() {
        val code = binding.registerInputCode.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code) /////

        AUTH.signInWithCredential(credential).addOnCompleteListener { /////
            if (it.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()

                val dataMap = mutableMapOf<String, Any>()
                dataMap[CHILD_ID] = uid
                dataMap[CHILD_PHONE] = phoneNumber
                dataMap[CHILD_USERNAME] = uid

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                    .addListenerForSingleValueEvent(AppValueEventListener {

                        if (!it.hasChild(CHILD_USERNAME)) {
                            dataMap[CHILD_USERNAME] = uid
                        }
                        REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                            .addOnFailureListener { showToast(it.message.toString()) }
                            .addOnSuccessListener {
                                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                                    .updateChildren(dataMap)
                                    .addOnSuccessListener {
                                        showToast("Welcome!")
                                        restartActivity()
                                    }
                                    .addOnFailureListener { showToast(it.message.toString()) }
                            }
                    })
            } else showToast(it.exception?.message.toString())
        }

    }
}