package com.example.planner.ui.menu

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.planner.R
import com.example.planner.databinding.ActivityMenuBinding
import com.example.planner.databinding.ActivitySignUpBinding
import com.example.planner.ui.authentication.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainMenu : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val signOut = binding.signOutButton

        val user = auth.currentUser
        checkIfLoggedIn(user)

        signOut.setOnClickListener {
            val alertDialog: AlertDialog = (this@MainMenu).let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setMessage("Are you sure you want to sign out?")
                    setPositiveButton(R.string.dialog_ok,
                        DialogInterface.OnClickListener { _, _ ->
                            // User clicked OK button
                            Firebase.auth.signOut()
                            redirectToLogin()
                        })
                    setNegativeButton(R.string.dialog_cancel,
                        DialogInterface.OnClickListener { dialog, _ ->
                            // User cancelled the dialog
                            dialog.dismiss()
                        })
                }
                // Create the AlertDialog
                builder.create()
            }

            alertDialog.show()
        }
    }

    /**
     * Checks if the user is logged in. If not, redirects to log in.
     */
    private fun checkIfLoggedIn(user: FirebaseUser?) {
        if (user == null) {
            redirectToLogin()
        }
    }

    /**
     * Redirects the user to the login activity.
     */
    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}