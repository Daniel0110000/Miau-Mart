package com.daniel.miaumart.domain.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class NetworkStateReceiver(
    private val layout: ConstraintLayout
): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if(!isConnected) layout.visibility = View.VISIBLE
        else layout.visibility = View.GONE
    }
}