package com.example.planner.ui.menu

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.planner.R
import com.example.planner.databinding.ActivityMenuBinding
import com.example.planner.databinding.ActivitySignUpBinding
import com.example.planner.ui.adapter.ItemAdapter
import com.example.planner.ui.authentication.LoginActivity
import com.example.planner.ui.locations.*
import com.example.planner.ui.network.RestClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import java.lang.Thread.sleep
import kotlin.math.log

class MainMenu : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val signOut = binding.signOutButton

        val user = auth.currentUser
//        checkIfLoggedIn(user)

        // Binds the sign out button
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


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = ItemAdapter(this, LoginActivity.locations)

        lifecycleScope.launch(Dispatchers.IO) {
            RestClient.getData("Athens")
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

