package com.example.planner.ui.locations

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Datasource {
    fun getLocations(): List<Location> {
        val db = Firebase.firestore
        val locationList = mutableListOf<Location>()

        db.collection("cities").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    locationList.add(Location(document["name"].toString()))
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        return locationList
    }
}