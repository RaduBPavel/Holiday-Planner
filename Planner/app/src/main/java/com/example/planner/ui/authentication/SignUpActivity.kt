package com.example.planner.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


        register.setOnClickListener {
            if (!checkCompletedFields(email, password, confirmedPassword)) {
                return@setOnClickListener
            }

            if (!checkMatchingPasswords(password, confirmedPassword)) {
                return@setOnClickListener
            }

            registerUser(email, password)
        }
    }

    /**
     * Checks if all the fields are completed.
     */
    private fun checkCompletedFields(
        email: EditText, password: EditText, confirmedPassword: EditText
    ) = TextUtils.isEmpty(email.text.toString()) and
            TextUtils.isEmpty(password.text.toString()) and
            TextUtils.isEmpty(confirmedPassword.text.toString())

    /**
     * Checks if the passwords match. If not, alerts the user.
     */
    private fun checkMatchingPasswords(password: EditText, confirmedPassword: EditText): Boolean {
        val passwordInput = password.text.toString()
        val confirmedPasswordInput = confirmedPassword.text.toString()

        if (passwordInput != confirmedPasswordInput) {
            Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_SHORT).show()
        }

        return passwordInput == confirmedPasswordInput
    }

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
                    println("ceva muie")
                    redirectToLogin()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
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