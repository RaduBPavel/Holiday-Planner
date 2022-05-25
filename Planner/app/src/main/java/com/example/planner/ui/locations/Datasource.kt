package com.example.planner.ui.locations

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class Response(
    var locations: MutableList<Location> = mutableListOf(),
    var exception: Exception? = null
)

class LocationDatasource {
    fun getResponseFromRealtimeDatabaseUsingLiveData(): MutableLiveData<Response> {
        val db = Firebase.firestore
        val mutableLiveData = MutableLiveData<Response>()

        db.collection("cities").get()
            .addOnCompleteListener { task ->
                val response = Response()

                for (document in task.result) {
                    val newLocation = Location(document["name"].toString())

                    response.locations.add(newLocation)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }

                mutableLiveData.value = response

                for (item in response.locations) {
                    print(item.name)
                }
                Log.i("firebase_test", "added_elements")
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                mutableLiveData.value = Response(exception = exception)
            }

        return mutableLiveData
    }
}

class LocationViewModel(
    private val datasource: LocationDatasource = LocationDatasource()
) : ViewModel() {
    fun getResponseUsingLiveData(): LiveData<Response> {
        return datasource.getResponseFromRealtimeDatabaseUsingLiveData()
    }
}