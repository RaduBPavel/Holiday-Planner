package com.example.planner.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.MutableBoolean
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.planner.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val email = binding.usernameSignup
        val password = binding.passwordSignup
        val confirmedPassword = binding.confirmPasswordSignup
        val register = binding.registerButton

        // Used for input validation
        val validEmail: MutableLiveData<Boolean> = MutableLiveData(false)
        val validPassword: MutableLiveData<Boolean> = MutableLiveData(false)
        val validConfirmedPassword: MutableLiveData<Boolean> = MutableLiveData(false)

        email.addTextChangedListener(EmailTextWatcher(email, validEmail))
        password.addTextChangedListener(PasswordTextWatcher(password, validPassword))
        confirmedPassword.addTextChangedListener(
            ConfirmedPasswordTextWatcher(
                confirmedPassword,
                password,
                validConfirmedPassword
            )
        )

        validConfirmedPassword.observe(this, Observer {
            if (checkValidInput(
                    validEmail.value!!,
                    validPassword.value!!,
                    validConfirmedPassword.value!!
                )
            ) {
                register.isEnabled = true
            }
        })

        register.setOnClickListener {
            registerUser(email, password)
        }
    }

    /**
     * Checks if all the fields have been validated.
     */
    private fun checkValidInput(
        validEmail: Boolean,
        validPassword: Boolean,
        validConfirmedPassword: Boolean
    ) =
        validEmail and validPassword and validConfirmedPassword

    /**
     * Registers the user. If the operation fails, alerts the user.
     */
    private fun registerUser(email: EditText, password: EditText) {
        val emailInput = email.text.toString()
        val passwordInput = password.text.toString()

        auth.createUserWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    redirectToLogin()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    /**
     * Redirects the user to the login activity.
     */
    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}