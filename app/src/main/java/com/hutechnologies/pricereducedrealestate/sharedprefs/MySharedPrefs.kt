package com.hutechnologies.pricereducedrealestate.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class MySharedPrefs(context: Context) {

    private val sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    fun saveEmail(email : String?) {
        sharedPref.edit().putString(USER_EMAIL, email).apply()
    }
    fun getEmail():String?{
        val email = sharedPref.getString(USER_EMAIL, null)
        return email
    }
    fun clearEmail() {
        sharedPref.edit().remove(USER_EMAIL).apply()
    }

    companion object{
        const val PREFS_NAME = "Amina_mart_Prefs"
        const val USER_EMAIL = "user_email"
    }
}