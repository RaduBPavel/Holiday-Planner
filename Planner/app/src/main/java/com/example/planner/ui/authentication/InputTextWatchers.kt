package com.example.planner.ui.authentication

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.MutableBoolean
import android.util.Patterns
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import java.util.regex.Pattern

class EmailTextWatcher(
    private val email: EditText,
    private var validEmail: MutableLiveData<Boolean>
) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        val text = p0.toString()
        if (TextUtils.isEmpty(text) or
            !Patterns.EMAIL_ADDRESS.matcher(text).matches()
        ) {
            email.error = "Invalid email"
            validEmail.value = false
        } else {
            validEmail.value = true
        }
    }
}

class PasswordTextWatcher(
    private val password: EditText,
    private var validPassword: MutableLiveData<Boolean>
) :
    TextWatcher {
    private val stringPattern by lazy { "(/^.{3,}\$/)" }
    private val pattern = Pattern.compile(stringPattern)

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        val text = p0.toString()
        if (TextUtils.isEmpty(text) or pattern.matcher(text).matches()) {
            password.error = "Password not secure enough"
            validPassword.value = false
        } else {
            validPassword.value = true
        }
    }
}

class ConfirmedPasswordTextWatcher(
    private val confirmedPassword: EditText,
    private val password: EditText,
    private var validConfirmedPassword: MutableLiveData<Boolean>
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        val passwordInput = password.text.toString()
        val confirmedPasswordInput = p0.toString()

        if (TextUtils.isEmpty(passwordInput)
            or TextUtils.isEmpty(confirmedPasswordInput)
            or (passwordInput != confirmedPasswordInput)
        ) {
            confirmedPassword.error = "Passwords do not match"
            validConfirmedPassword.value = false
        } else {
            validConfirmedPassword.value = true
        }
    }
}