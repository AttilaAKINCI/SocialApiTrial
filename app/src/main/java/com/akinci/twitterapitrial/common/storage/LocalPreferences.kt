package com.akinci.twitterapitrial.common.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalPreferences @Inject constructor(
        @ApplicationContext val context : Context
) : Preferences {
    private val prefTag  = context.packageName + "_preferences"
    private val prefs = context.getSharedPreferences(prefTag, Context.MODE_PRIVATE)

    override fun getStoredTag(key: String): String? {
        return prefs.getString(key, "")
    }
    override fun setStoredTag(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}