package com.akinci.socialapitrial.common.component

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class SnackBar {
    companion object {
        //TODO remove unused
        const val LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE
        const val LENGTH_SHORT = Snackbar.LENGTH_SHORT
        const val LENGTH_LONG = Snackbar.LENGTH_LONG

        //TODO private
        fun make(view: View, text: CharSequence, duration: Int): Snackbar {
            // log every snackBar messages
            Timber.d(text.toString())
            return Snackbar.make(view, text, duration)
        }

        fun makeLarge(view: View, text: CharSequence, duration: Int): Snackbar {
            //TODO you can use "apply"
            return make(view, text, duration).apply {
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 8
            }
        }
    }
}