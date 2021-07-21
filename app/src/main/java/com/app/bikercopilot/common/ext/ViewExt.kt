package com.app.bikercopilot.common.ext

import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    removeObservers(liveData)
    liveData.observe(this, Observer(body))
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.removeObservers(liveData: L) {
    liveData.removeObservers(this)
}

fun TextInputEditText.getValue(): String {
    return this.text.toString()
}

fun TextInputLayout.clearError() {
    this.error?.let {
        this.error = null
    }
}

fun TextInputLayout.putError(error: String) {
    this.isErrorEnabled = true
    this.error = error
}

fun TextInputEditText.enableAutoClearError() {
    val possibleTIL = this.parent.parent
    if (possibleTIL is TextInputLayout) {
        this.doOnTextChanged { _, _, _, _ ->
            possibleTIL.clearError()
        }
    }
}
