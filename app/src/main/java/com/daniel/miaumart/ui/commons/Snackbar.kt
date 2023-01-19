package com.daniel.miaumart.ui.commons

import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar


class Snackbar {

    companion object {
        fun showMessage(message: String, layout: ConstraintLayout) {
            val snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT)
            val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            snackbar.show()
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(Color.parseColor("#212020"))
            snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

}