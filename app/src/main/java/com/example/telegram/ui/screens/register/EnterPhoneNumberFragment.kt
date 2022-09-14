package com.example.telegram.ui.screens.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.telegram.R
import com.example.telegram.database.AUTH
import com.example.telegram.databinding.FragmentEnterPhoneNumberBinding
import com.example.telegram.utilits.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


/*
class EnterPhoneNumberFragment : Fragment() {

    private lateinit var mBinding: FragmentEnterPhoneNumberBinding
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks ////
    private lateinit var mNumber: String
    private val TAG = "TAG"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {    /////
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                AUTH.signInWithCredential(credential).addOnCompleteListener {   /////
                    if (it.isSuccessful) {
                        showToast("Welcome!")
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    } else showToast(it.exception?.message.toString())


                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
                Log.d(TAG, "onVerificationFailed: ${p0.localizedMessage}")
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mNumber, id))
            }
        }

        mBinding.registerBtnNext.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (mBinding.registrInputPhoneNumber.text.toString().isEmpty()) {
            showToast(getString(R.string.register_tost_enter_phone))
        } else {
            authUser()
        }
    }

    fun authUser() {
        mNumber = mBinding.registrInputPhoneNumber.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(   //////
            mNumber, 60, TimeUnit.SECONDS, (activity as RegisterActivity), mCallback
        )
        Log.d(TAG, "authUser: $mNumber")
    }
}*/



class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks    // / / /
    private lateinit var mBinding: FragmentEnterPhoneNumberBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()                                                               // / / / / //
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Добро пожаловать")
                        restartActivity()
                    } else showToast(task.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
                Log.d("TAG", "onVerificationFailed: ${p0.message.toString()}")
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
                Log.d("TAG", "onVerificationFailed: $id")
            }
        }
        mBinding.registerBtnNext.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (mBinding.registrInputPhoneNumber.text.toString().isEmpty()) {
            showToast("getString(R.string.register_toast_enter_phone)")
        } else {
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = mBinding.registrInputPhoneNumber.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(                           /// / / / /
            mPhoneNumber,
            60,
            TimeUnit.SECONDS,
            APP_ACTIVITIY,
            mCallback
        )
        Log.d("TAG", "authUser: $mPhoneNumber")
    }
}