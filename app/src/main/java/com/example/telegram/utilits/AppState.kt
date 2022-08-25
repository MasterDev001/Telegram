package com.example.telegram.utilits

import com.example.telegram.database.*

enum class AppState(val state: String) {
    ONLINE("online"),
    OFFLINE("offline"),
    TYPING("typing");

    companion object {
        fun updateState(appState: AppState) {
            if (AUTH.currentUser!=null)
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATE)
                .setValue(appState.state)
                .addOnSuccessListener { USER.state = appState.state }
                .addOnFailureListener { showToast(it.message.toString()) }
        }
    }
}