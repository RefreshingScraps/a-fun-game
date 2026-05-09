package com.freshingair.afungame

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.widget.TextView
import androidx.core.content.ContextCompat

object DialogUtils {
    fun getAlertDialog(context: Context?, isLight: Boolean): AlertDialog.Builder {
        return AlertDialog.Builder(
            ContextThemeWrapper(context, getDialogTheme(isLight))
        )
    }
    @Suppress("DEPRECATION")
    fun getDialogTheme(isLight: Boolean): Int {
        return (if (isLight)
            R.style.Theme_Holo_Light_Dialog
        else
            R.style.Theme_Holo_Dialog)
    }

    fun setAlertDialog(context: Context, dlg: AlertDialog, titleColor: Int?){ // messageColor: Int?, btnColor: Int?)
        setAlertDialogTitle(context, dlg, titleColor ?: ContextCompat.getColor(context, R.color.holo_blue_bright))
        // setAlertDialogMessage(dlg, messageColor ?: ContextCompat.getColor(context, R.color.holo_blue_dark))
        // setAlertDialogBtnView(dlg, btnColor ?: ContextCompat.getColor(context, R.color.holo_blue_dark))
    }

     // android.R.color.holo_blue_dark
    @SuppressLint("DiscouragedApi")
    private fun setAlertDialogTitle(context: Context, dlg: AlertDialog, titleColor: Int) {
        val alertTitleId = context.resources.getIdentifier("alertTitle", "id", "android")
        val title: TextView = dlg.findViewById(alertTitleId)
        title.setTextColor(titleColor)
    }
     // android.R.color.holo_blue_dark
//    private fun setAlertDialogMessage(dlg: AlertDialog, messageColor: Int) {
//        val message: TextView = dlg.findViewById(R.id.message)
//        message.setTextColor(messageColor)
//    }
     // android.R.color.holo_blue_dark
    // private fun setAlertDialogBtnView(dlg: AlertDialog, btnColor: Int) {
        // val btn = dlg.getButton(DialogInterface.BUTTON_POSITIVE) as Button
        // btn.setTextColor(btnColor)
    // }
}