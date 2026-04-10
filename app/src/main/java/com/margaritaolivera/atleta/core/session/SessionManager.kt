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

    fun saveUserId(userId: Int) {
        prefs.edit().putInt("USER_ID", userId).apply()
    }

    fun getUserId(): Int = prefs.getInt("USER_ID", -1)

    fun clearWorkoutStats() {
        prefs.edit()
            .remove("last_stat_SQUAT")
            .remove("last_stat_RUN")
            .remove("last_stat_JOG")
            .apply()
    }

    fun clearAllWorkoutData() {
        prefs.edit()
            .remove("goal_SQUAT")
            .remove("goal_RUN")
            .remove("goal_JOG")
            .remove("last_stat_SQUAT")
            .remove("last_stat_RUN")
            .remove("last_stat_JOG")
            .remove("DUELS_TOTAL")
            .remove("DUELS_WON")
            .apply()
    }

    fun getName(): String? = prefs.getString("NAME", null)

    fun getLevel(): Int = prefs.getInt("LEVEL", 1)

    fun getXp(): Int = prefs.getInt("XP", 0)

    fun logout() {
        prefs.edit()
            .remove("TOKEN")
            .remove("NAME")
            .remove("LEVEL")
            .remove("XP")
            .remove("USER_ID")
            .apply()
    }

    fun getGoal(type: String): Int {
        return prefs.getInt("goal_$type", if (type == "SQUAT") 20 else 1000) // 20 reps o 1km por defecto
    }

    fun saveGoal(type: String, value: Int) {
        prefs.edit().putInt("goal_$type", value).apply()
    }

    fun saveLastWorkoutStat(type: String, value: Float) {
        prefs.edit().putFloat("last_stat_$type", value).apply()
    }

    fun getLastSquatReps(): Int = prefs.getFloat("last_stat_SQUAT", 0f).toInt()

    fun getLastRunDistance(): Float {
        val run = prefs.getFloat("last_stat_RUN", -1f)
        val jog = prefs.getFloat("last_stat_JOG", -1f)
        return when {
            run < 0 && jog < 0 -> 0f
            run < 0 -> jog
            jog < 0 -> run
            else -> maxOf(run, jog)
        }
    }

    fun incrementDuelsPlayed(won: Boolean) {
        val total = prefs.getInt("DUELS_TOTAL", 0) + 1
        var wins = prefs.getInt("DUELS_WON", 0)
        if (won) wins += 1
        prefs.edit()
            .putInt("DUELS_TOTAL", total)
            .putInt("DUELS_WON", wins)
            .apply()
    }

    fun getDuelsWon(): Int = prefs.getInt("DUELS_WON", 0)

    fun getDuelsTotal(): Int = prefs.getInt("DUELS_TOTAL", 0)
}