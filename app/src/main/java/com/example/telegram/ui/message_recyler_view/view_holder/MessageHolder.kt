package com.example.telegram.ui.message_recyler_view.view_holder

import com.example.telegram.ui.message_recyler_view.views.MessageView

interface MessageHolder {
    fun drawMessage(view:MessageView)
    fun onAttach(view:MessageView)
    fun onDetach()
}