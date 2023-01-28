package com.daniel.miaumart.ui.commons

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.app.AlertDialog
import com.daniel.miaumart.R
import org.w3c.dom.Text

class AlertDialog {
    companion object {
        fun buildAlertDialog(
            title: String,
            message: String,
            context: Context,
            listener: (Boolean) -> Unit
        ) {
            val build = AlertDialog.Builder(context)
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.alert_dialog, null)
            build.setView(view)
            val alertDialog = build.create()
            alertDialog.show()
            view.findViewById<TextView>(R.id.alert_dialog_title).text = title
            view.findViewById<TextView>(R.id.alert_dialog_message).text = message
            view.findViewById<Button>(R.id.positive_button).setOnClickListener {
                listener(true)
                alertDialog.dismiss()
            }
            view.findViewById<Button>(R.id.negative_button).setOnClickListener {
                listener(false)
                alertDialog.dismiss()
            }

        }
    }
}