package com.example.planner.ui.network

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.planner.R
import com.example.planner.ui.authentication.LoginActivity
import com.example.planner.ui.menu.MainMenu


class NetworkConnectivityReceiver: BroadcastReceiver() {
    var dialog: Dialog? = null
    var nettext: TextView? = null
    val networkUtil: NetworkUtil = NetworkUtil()

    override fun onReceive(context: Context, intent: Intent?) {
        var status: String = networkUtil.getConnectivityStatusString(context)!!
        dialog = Dialog(context, android.R.style.Theme_NoTitleBar_Fullscreen)
        dialog!!.setContentView(R.layout.no_internet)
        val restartApp: Button = dialog!!.findViewById(R.id.restartapp) as Button
        nettext = dialog!!.findViewById(R.id.nettext)
        restartApp.setOnClickListener {
            (context as Activity).finish()
            val i = Intent(context, LoginActivity::class.java)
            context.startActivity(i)
        }
        if (status.isEmpty() || status == "No internet is available" || status == "No Internet Connection") {
            status = "No Internet Connection"
            dialog!!.show()
        }
        Toast.makeText(context, status, Toast.LENGTH_LONG).show()
    }
}