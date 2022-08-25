package com.example.telegram.ui.screens.single_chat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.telegram.R
import com.example.telegram.database.*
import com.example.telegram.databinding.FragmentSingleChatBinding
import com.example.telegram.model.CommonModel
import com.example.telegram.model.UserModel
import com.example.telegram.ui.screens.base.BaseFragment
import com.example.telegram.ui.message_recyler_view.views.AppViewFactory
import com.example.telegram.ui.screens.main_list.MainListFragment
import com.example.telegram.utilits.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mBinding: FragmentSingleChatBinding
    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener
    private lateinit var mAdaptor: SingleChatAdaptor
    private var mCountMessages = 15
    private var mIsScrooling = false
    private var mSmoothScroollPosition = true
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        initFields()
        initToolbar()
        initRecyclerView()
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun initFields() {
        setHasOptionsMenu(true)
        mBottomSheetBehavior =
            BottomSheetBehavior.from(requireView().findViewById(R.id.bottom_sheet_choice))
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        mAppVoiceRecorder = AppVoiceRecorder()
        mSwipeRefreshLayout = mBinding.chatSwipeRefresh
        mLayoutManager = LinearLayoutManager(this.context)
        mBinding.chatInputMessage.addTextChangedListener(AppTextWatcher {      //// text change bolganda esitadi va ichi ishga tushadi
            val string = mBinding.chatInputMessage.text.toString()
            if (string.isEmpty() || string == "Recording") {
                mBinding.chatBtnSendMessage.visibility = View.GONE
                mBinding.chatBtnAttach.visibility = View.VISIBLE
                mBinding.chatBtnVoice.visibility = View.VISIBLE
            } else {
                mBinding.chatBtnSendMessage.visibility = View.VISIBLE
                mBinding.chatBtnAttach.visibility = View.GONE
                mBinding.chatBtnVoice.visibility = View.GONE
            }
        })
        mBinding.chatBtnAttach.setOnClickListener { attach() }

        CoroutineScope(Dispatchers.IO).launch {                                    ///// / // / /

            mBinding.chatBtnVoice.setOnTouchListener { v, event ->                           ////// /// / / /
                if (checkPermission(RECORD_AUDIO)) {
                    if (event.action == MotionEvent.ACTION_DOWN) {        //// bosilganda Record qiladi
                        mBinding.chatInputMessage.setText("Recording")
                        mBinding.chatBtnVoice.setColorFilter(   /// btn voice bosilganda rangi colorPrimaryga o'zgaradi
                            ContextCompat.getColor(
                                APP_ACTIVITIY,
                                R.color.colorPrimary
                            )
                        )
                        val messageKey = getMessageKey(contact.id)
                        mAppVoiceRecorder.startRecorder(messageKey)
                    } else if (event.action == MotionEvent.ACTION_UP) {
                        mBinding.chatInputMessage.setText("")
                        mBinding.chatBtnVoice.colorFilter = null
                        mAppVoiceRecorder.stopRecorder { file, messageKey ->
                            uploadFileToStorage(
                                Uri.fromFile(file),                                //// // / / / / /
                                messageKey,
                                contact.id,
                                TYPE_MESSAGE_VOICE
                            )
                            mSmoothScroollPosition = true
                        }
                    }
                }
                true
            }
        }
    }

    private fun attach() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        view?.findViewById<ImageView>(R.id.btn_attach_file)?.setOnClickListener { attachFile() }
        view?.findViewById<ImageView>(R.id.btn_attach_image)?.setOnClickListener { attachImage() }

    }

    private fun attachFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)                            ///      //   ///
        intent.type = "*/*"                                                           // / / / / / /
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)                       // / / / / // /
    }

    private fun attachImage() {
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setRequestedSize(250, 250)
                setAspectRatio(1, 1)
            }
        )
    }

    private fun initRecyclerView() {
        mRecyclerView = mBinding.chatRecyclerView
        mAdaptor = SingleChatAdaptor()
        mRecyclerView.adapter = mAdaptor
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.setHasFixedSize(true)                                           ////// / //
        mRecyclerView.isNestedScrollingEnabled = false                                /// // / //

        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(CURRENT_UID).child(contact.id)

        mMessagesListener = AppChildEventListener {
            val message = it.getCommonModel()

            if (mSmoothScroollPosition) {
                mAdaptor.addItemToBottom(AppViewFactory.getView(message)) {
                    mRecyclerView.smoothScrollToPosition(mAdaptor.itemCount)   //// / /  recy view yangilanadi sms jonatganda pastga tushadi
                }
            } else {
                mAdaptor.addItemToTop(AppViewFactory.getView(message)) {
                    mSwipeRefreshLayout.isRefreshing = false
                }
            }
        }
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {      //////////
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrooling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3) {  ///ml.findf=binrinchi koringam item 3 dan kichik bolsa
                    println("sf")
                    updateData()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    mIsScrooling = true
            }
        })
        mSwipeRefreshLayout.setOnRefreshListener { updateData() }
    }

    private fun updateData() {
        mSmoothScroollPosition = false
        mIsScrooling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
    }


    private fun initToolbar() {
        mToolbarInfo = APP_ACTIVITIY.mToolbar
        mToolbarInfo.findViewById<ConstraintLayout>(R.id.toolbar_info1).visibility = View.VISIBLE

        mListenerInfoToolbar = AppValueEventListener {     //// // // / / / //
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addListenerForSingleValueEvent(mListenerInfoToolbar)
        mBinding.chatBtnSendMessage.setOnClickListener {
            mSmoothScroollPosition = true
            val message = mBinding.chatInputMessage.text.toString()
            if (message.isEmpty()) {
                showToast("Write Text")
            } else {
                saveToMainList(contact.id, TYPE_CHAT)
                sendMessage(message, contact.id, TYPE_TEXT) {
                    mBinding.chatInputMessage.setText("")
                }
            }
        }
    }

    private fun initInfoToolbar() {
        if (mReceivingUser.fullname.isEmpty()) {
            mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_fullname).text =
                contact.fullname
        } else {
            mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_fullname).text =
                mReceivingUser.fullname
        }

        mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_status).text = mReceivingUser.state
        mToolbarInfo.findViewById<CircleImageView>(R.id.toolbar_chat_image)
            .photoDownloadAndSet(mReceivingUser.photoUrl)
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.findViewById<ConstraintLayout>(R.id.toolbar_info1).visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbar)
        mRefMessages.removeEventListener(mMessagesListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAppVoiceRecorder.releaseRecorder()
        mAdaptor.onDestroy()
    }

    private val cropImage =
        registerForActivityResult(CropImageContract()) { result ->       ////// / / / //
            val uri = result.originalUri
            val messageKey = getMessageKey(contact.id)
            if (uri != null) {
                uploadFileToStorage(uri, messageKey, contact.id, TYPE_MESSAGE_IMAGE)
                mSmoothScroollPosition = true
            }
        }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {    // / // / /
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode== PICK_FILE_REQUEST_CODE){
        val uri = data?.data
        val fileName = getFileNameFromUri(uri)
        val messageKey = getMessageKey(contact.id)
        if (uri != null) {
            uploadFileToStorage(uri, messageKey, contact.id, TYPE_MESSAGE_FILE, fileName)
            mSmoothScroollPosition = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {                     /////
        activity?.menuInflater?.inflate(R.menu.single_chat_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear_chat -> clearChat(contact.id) {
                showToast("Cleared Chat")
                replaceFragment(MainListFragment())
            }
            R.id.menu_delete_chat -> deleteChat(contact.id) {
                showToast("Deleted Chat")
                replaceFragment(MainListFragment())
            }
        }
        return true
    }
}

