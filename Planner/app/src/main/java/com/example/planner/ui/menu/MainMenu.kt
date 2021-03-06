package com.example.planner.ui.menu

import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planner.R
import com.example.planner.databinding.ActivityMenuBinding
import com.example.planner.ui.adapter.ItemAdapter
import com.example.planner.ui.adapter.onItemClick
import com.example.planner.ui.authentication.LoginActivity
import com.example.planner.ui.authentication.LoginActivity.Companion.locations
import com.example.planner.ui.locations.Location
import com.example.planner.ui.locations.LocationFragment
import com.example.planner.ui.network.NetworkConnectivityReceiver
import com.example.planner.ui.network.RestClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*


class MainMenu : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var broadcastReceiver: BroadcastReceiver
    private var subscriberCities = mutableListOf<Location>()
    private var fragmentOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val signOut = binding.signOutButton

        val user = auth.currentUser
        checkIfLoggedIn(user)

        // Gets the list of subscribed cities
        for (location in locations) {
            if (user!!.email in location.subscribers) {
                subscriberCities.add(location)
            }
        }

        // Binds the broadcast receiver
        broadcastReceiver = NetworkConnectivityReceiver()
        broadcastIntent()

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


        // Instantiates the recycler view and updates periodically
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val allLocationsAdapter = ItemAdapter(this, LoginActivity.locations)
        val subscribedLocationsAdapter = ItemAdapter(this, subscriberCities)
        recyclerView.adapter = allLocationsAdapter

        updateItems(recyclerView)

        // Creates the fragment on click
        val fragment : LocationFragment = LocationFragment.newInstance()
        recyclerView.onItemClick { _, position, _ ->
            // for passing data to fragment
            val bundle = Bundle()
            bundle.putString("item_title", locations[position].name)
            bundle.putDouble("item_temp", locations[position].temperature)
            bundle.putInt("item_humidity", locations[position].humidity)
            bundle.putBoolean("is_day", locations[position].isDay)
            bundle.putDouble("item_air_temp", locations[position].airTemperature)
            bundle.putDouble("item_wind", locations[position].windSpeed)

            fragment.arguments = bundle
            if (!fragmentOn && savedInstanceState == null) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, "fragment_name")
                    .commit()

                fragmentOn = true
            }
        }

        // Removes the fragment on click outside of it
        binding.root.setOnClickListener{ view ->
            if (fragmentOn && view.id != R.id.fragment_container) {
                supportFragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit()

                supportFragmentManager
                    .popBackStack()

                fragmentOn = false
            }
        }

        // Binds the menu buttons
        val mainMenuButton = binding.mainMenuButton
        mainMenuButton.alpha = 1.0f

        val myCitiesButton = binding.myCitiesButton
        myCitiesButton.alpha = 0.5f

        mainMenuButton.setOnClickListener {
            recyclerView.swapAdapter(allLocationsAdapter, false)
            mainMenuButton.alpha = 1.0f
            myCitiesButton.alpha = 0.5f
        }

        myCitiesButton.setOnClickListener {
            recyclerView.swapAdapter(subscribedLocationsAdapter, false)
            mainMenuButton.alpha = 0.5f
            myCitiesButton.alpha = 1.0f
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

    /**
     * Periodically make calls to the Weather API to get changes
     */
    private fun updateItems(recyclerView: RecyclerView) {
        val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

        lifecycleScope.launch {
            while (true) {
                delay(5000)
                if (isNetworkAvailable()) {
                    for (location in locations) {
                        CoroutineScope(Dispatchers.IO).launch {
                            location.updateValues((RestClient))
                        }
                    }
                    for (index in layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastCompletelyVisibleItemPosition()) {
                        val adapter = recyclerView.adapter as ItemAdapter
                        adapter.modifyItem(index)
                    }
                }
            }
        }
    }

    // Used to bind and unbind the broadcast receiver
    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}

