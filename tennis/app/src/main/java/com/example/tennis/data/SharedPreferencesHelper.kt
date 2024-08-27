package com.example.tennis.data

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {

    private const val PREFS_NAME = "starred"

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun addCourtToStars(context: Context, courtPlace: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val starredSet = sharedPreferences.getStringSet("starred_courts", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        starredSet.add(courtPlace)
        editor.putStringSet("starred_courts", starredSet)
        editor.apply()
    }

    fun removeCourtFromStars(context: Context, courtPlace: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val starredSet = sharedPreferences.getStringSet("starred_courts", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        starredSet.remove(courtPlace)
        editor.putStringSet("starred_courts", starredSet)
        editor.apply()
    }

    fun isCourtStarred(context: Context, courtPlace: String): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        val starredSet = sharedPreferences.getStringSet("starred_courts", mutableSetOf())
        return starredSet?.contains(courtPlace) ?: false
    }
}
