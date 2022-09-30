package com.example.telegram.ui.screens.settings

import android.os.Bundle
import android.view.*
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.telegram.R
import com.example.telegram.database.*
import com.example.telegram.databinding.FragmentSettingsBinding
import com.example.telegram.ui.screens.base.BaseFragment
import com.example.telegram.utilits.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        setHasOptionsMenu(true)   /// oncreateOptionsMenuni ishlatyabdi
        initFields()
    }

    private fun initFields() {
        binding.settingsBio.text = USER.bio
        binding.settingsPhoneNumber.text = USER.phone
        binding.settingsFullName.text = USER.fullname
        binding.settingsUsername.text = USER.usereame
        binding.settingsStatus.text = USER.state
        binding.settingsBtnChangeUsername.setOnClickListener {
            replaceFragment(ChangeUserNameFragment())
        }
        binding.settingsBtnChangeBio.setOnClickListener {
            replaceFragment(ChangeBioFragment())
        }
        binding.settingsChangePhoto.setOnClickListener { changePhoto() }
        binding.settingsUserPhoto.photoDownloadAndSet(USER.photoUrl)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {   ///
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                AppState.updateState(AppState.OFFLINE)
                restartActivity()
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        val uri = result.originalUri
        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
            .child(CURRENT_UID)

        if (uri != null) {
            putFileToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDatabase(it) {
                        binding.settingsUserPhoto.photoDownloadAndSet(it)
                        USER.photoUrl = it
                        showToast("Image Changed!")
                        APP_ACTIVITIY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
        /* path.putFile(uri!!).addOnCompleteListener { task1 ->
             if (task1.isSuccessful) {
                 path.downloadUrl.addOnCompleteListener { task2 ->
                     if (task2.isSuccessful) {
                         val photoUrl = task2.result.toString()
                         REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UiD)
                             .child(CHILD_PHOTO_URL).setValue(photoUrl)
                             .addOnCompleteListener { task3 ->
                                 if (task3.isSuccessful) {
                                     binding.settingsUserPhoto.photoDownloadAndSet(photoUrl)
                                     USER.photoUrl = photoUrl
                                 }
                             }
                     }
                 }
             }
         }*/
    }

    private fun changePhoto() {
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setCropShape(CropImageView.CropShape.OVAL)
                setRequestedSize(600, 600)
                setAspectRatio(1, 1)
            }
        )
    }

}