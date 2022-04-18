package com.example.planner.ui.authentication

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import java.util.regex.Pattern

class EmailTextWatcher(
    private val email: EditText,
    private val validEmail: MutableLiveData<Boolean>
) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        val emailInput = p0.toString()
        if (TextUtils.isEmpty(emailInput) or
            !Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
        ) {
            email.error = "Invalid email"
            validEmail.value = false
            return
        }

        validEmail.value = true
    }
}

class PasswordTextWatcher(
    private val password: EditText,
    private val validPassword: MutableLiveData<Boolean>,
    private val errorMessage: String
) :
    TextWatcher {
    private val stringPattern by lazy { "(/^.{3,}\$/)" }
    private val pattern = Pattern.compile(stringPattern)

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        val passwordInput = p0.toString()
        if (TextUtils.isEmpty(passwordInput) or pattern.matcher(passwordInput).matches()) {
            password.error = errorMessage
            validPassword.value = false
            return
        }

        validPassword.value = true
    }
}

class ConfirmedPasswordTextWatcher(
    private val confirmedPassword: EditText,
    private val password: EditText,
    private val validConfirmedPassword: MutableLiveData<Boolean>
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
            return
        }

        validConfirmedPassword.value = true
    }
}