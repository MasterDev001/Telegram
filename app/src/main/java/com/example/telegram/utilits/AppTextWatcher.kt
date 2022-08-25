package com.example.telegram.utilits

import android.text.Editable
import android.text.TextWatcher

class AppTextWatcher(val onSuccess: (Editable?) -> Unit) : TextWatcher {   ////// funksiy editabele qabul qilib unit(void) (qalytaryabdi)
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        onSuccess(s)
    }
}