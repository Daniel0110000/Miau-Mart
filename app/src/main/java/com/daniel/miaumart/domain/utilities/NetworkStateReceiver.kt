package com.daniel.miaumart.domain.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.daniel.miaumart.R

class NetworkStateReceiver(
    private val layout: ConstraintLayout
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (!isConnected(context)) {
            layout.animation = AnimationUtils.loadAnimation(context, R.anim.open_layout_animation)
            layout.visibility = View.VISIBLE
        } else {
            layout.animation = AnimationUtils.loadAnimation(context, R.anim.close_layout_animation)
            layout.visibility = View.GONE
        }
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}