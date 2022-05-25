package com.example.planner.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.planner.databinding.ActivityLoginBinding
import com.example.planner.ui.locations.Location
import com.example.planner.ui.menu.MainMenu
import com.example.planner.ui.network.RestClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "LogIn Activity"
    private val jobList = mutableListOf<Job>()

    companion object {
        val locations = mutableListOf<Location>()
    }

    private val locationNames =
        listOf("Bucharest", "Athens", "Amsterdam", "Los Angeles", "Shanghai")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val email = binding.username
        val password = binding.password
        val login = binding.loginButton
        val signup = binding.signupButton


        // Used for data validation
        val validEmail: MutableLiveData<Boolean> = MutableLiveData(false)
        val validPassword: MutableLiveData<Boolean> = MutableLiveData(false)

        email.addTextChangedListener(EmailTextWatcher(email, validEmail))
        password.addTextChangedListener(
            PasswordTextWatcher(
                email,
                validPassword,
                "Invalid password entered"
            )
        )

        validPassword.observe(this, Observer {
            login.isEnabled = checkValidInput(
                validEmail.value!!,
                validPassword.value!!
            )
        })

        signup!!.setOnClickListener {
            redirectToSignUp()
        }

        login.setOnClickListener {
            loginUser(email, password)
        }

        // Start the background thread for collecting data
        val db = Firebase.firestore
        db.collection("cities")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    locations.add(Location(document.getString("name")!!))
                }

                locations.sortBy { it.name }
                for (location in locations) {
                    CoroutineScope(Dispatchers.IO).launch {
                        location.updateValues((RestClient))
                    }
                }
            }

//        locationNames.forEach { locations.add(Location(it)) }
    }

    /**
     * Checks if all the fields have been validated.
     */
    private fun checkValidInput(
        validEmail: Boolean,
        validPassword: Boolean
    ) =
        validEmail and validPassword

    /**
     * Redirects the user to the sign up activity.
     */
    private fun redirectToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    /**
     * Redirects the user to the main menu activity.
     */
    private fun redirectToMenu() {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }

    /**
     * Signs in the user. If the operation fails, alert the user.
     */
    private fun loginUser(email: EditText, password: EditText) {
        val emailInput = email.text.toString()
        val passwordInput = password.text.toString()

        auth.signInWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    redirectToMenu()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}