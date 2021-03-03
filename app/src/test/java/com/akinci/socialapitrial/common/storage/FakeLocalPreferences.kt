package com.akinci.socialapitrial.common.storage

//TODO asil implementation degisirse, bu test anlamsiz kalabilir
class FakeLocalPreferences : Preferences {
    private val storage : HashMap<String, String?> = hashMapOf()

    override fun getStoredTag(key: String): String? { return storage[key] }
    override fun setStoredTag(key: String, value: String) { storage[key] = value }
}