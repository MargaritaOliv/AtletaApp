package com.margaritaolivera.atleta.core.session

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("atleta_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("TOKEN", token).apply()
    }

    fun getToken(): String? = prefs.getString("TOKEN", null)

    fun saveFcmToken(token: String) {
        prefs.edit().putString("FCM_TOKEN", token).apply()
    }

    fun getFcmToken(): String? = prefs.getString("FCM_TOKEN", null)

    fun saveProfile(name: String, level: Int, xp: Int) {
        prefs.edit()
            .putString("NAME", name)
            .putInt("LEVEL", level)
            .putInt("XP", xp)
            .apply()
    }

    fun getName(): String? = prefs.getString("NAME", null)

    fun getLevel(): Int = prefs.getInt("LEVEL", 1)

    fun getXp(): Int = prefs.getInt("XP", 0)

    fun logout() {
        prefs.edit().clear().apply()
    }
}