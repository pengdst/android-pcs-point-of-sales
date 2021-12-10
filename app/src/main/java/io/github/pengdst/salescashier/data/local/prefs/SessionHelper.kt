package io.github.pengdst.salescashier.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SessionHelper private constructor(context: Context){

    private val pref = PreferenceManager.getDefaultSharedPreferences(context)

    fun save(key: String, value: String?) {
        pref.edit().putString(key, value).apply()
    }

    fun save(key: String, value: Boolean) {
        pref.edit().putBoolean(key, value).apply()
    }

    fun save(key: String, value: Float) {
        pref.edit().putFloat(key, value).apply()
    }

    fun save(key: String, value: Int?) {
        pref.edit().putInt(key, value ?: -1).apply()
    }

    fun save(key: String, value: Long) {
        pref.edit().putLong(key, value).apply()
    }

    fun getString(key: String, defValue: String? = "") = pref.getString(key, defValue)
    fun getInt(key: String, defValue: Int = 0) = pref.getInt(key, defValue)
    fun getFloat(key: String, defValue: Float = 0F) = pref.getFloat(key, defValue)
    fun getLong(key: String, defValue: Long = 0) = pref.getLong(key, defValue)
    fun getBoolean(key: String, defValue: Boolean = false) = pref.getBoolean(key, defValue)
    fun destroy() = pref.edit().clear().apply()

    fun register(listener: SharedPreferences.OnSharedPreferenceChangeListener) = pref.registerOnSharedPreferenceChangeListener(listener)
    fun unRegister(listener: SharedPreferences.OnSharedPreferenceChangeListener) = pref.unregisterOnSharedPreferenceChangeListener(listener)

    companion object {

        @Volatile
        private var INSTANCE: SessionHelper? = null

        fun newInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: SessionHelper(context).also { INSTANCE = it }
        }

    }
}