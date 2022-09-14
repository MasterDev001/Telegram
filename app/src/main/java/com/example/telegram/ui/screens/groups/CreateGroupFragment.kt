package com.example.telegram.ui.screens.groups

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.telegram.R
import com.example.telegram.database.*
import com.example.telegram.databinding.FragmentCreateGroupBinding
import com.example.telegram.model.CommonModel
import com.example.telegram.ui.screens.base.BaseFragment
import com.example.telegram.ui.screens.main_list.MainListFragment
import com.example.telegram.utilits.*
import java.net.URI

class CreateGroupFragment(private val listContacts: List<CommonModel>) :
    BaseFragment(R.layout.fragment_create_group) {

    private lateinit var mBinding: FragmentCreateGroupBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private var mUri = Uri.EMPTY                                                  // // / // / /

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITIY.title = "Create Group"
        hideKeyboard()
        initRecyclerView()
        mBinding.createGroupBtnComplete.setOnClickListener {
            val nameGroup = mBinding.createGroupInputName.text.toString()
            if (nameGroup.isEmpty()) {
                showToast("Write Name Group")
            } else {
                createGroupToDatabase(nameGroup, mUri, listContacts) {
                    replaceFragment(MainListFragment())
                }
            }
        }
        mBinding.createGroupInputName.requestFocus() //srazi editga tayyorledi edit bo'lm,asaxam yonib turadi
        mBinding.createGroupCount.text = getPlurals(listContacts.size)                    //// / / /
        mBinding.createGroupPhoto.setOnClickListener { addPhoto() }
    }

    private fun initRecyclerView() {
        mRecyclerView = mBinding.createGroupRecyclerView
        mAdapter = AddContactsAdapter()
        mRecyclerView.adapter = mAdapter

        listContacts.forEach { mAdapter.updateListItems(it) }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->  //result rasm
        mUri = result.originalUri//orginal|Uri kesishtayorlangan rasm urisi
        mBinding.createGroupPhoto.setImageURI(mUri)//iimageni urisi bo'yicha set qiladi
    }

    private fun addPhoto() {
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)//rasm kesibturgandagi chiziqlarni ko'rsatyabdi
                setCropShape(CropImageView.CropShape.OVAL)
                setRequestedSize(600, 600)
                setAspectRatio(1, 1)
            }
        )
    }
}