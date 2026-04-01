package com.margaritaolivera.atleta.core.session

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private var _token: String? = null
    private var _fcmToken: String? = null

    private var _name: String? = null
    private var _level: Int = 1
    private var _xp: Int = 0

    fun saveToken(token: String) { _token = token }
    fun getToken(): String? = _token

    fun saveFcmToken(token: String) { _fcmToken = token }
    fun getFcmToken(): String? = _fcmToken

    fun saveProfile(name: String, level: Int, xp: Int) {
        _name = name
        _level = level
        _xp = xp
    }

    fun getName(): String? = _name
    fun getLevel(): Int = _level
    fun getXp(): Int = _xp

    fun logout() { 
        _token = null 
        _name = null
        _level = 1
        _xp = 0
    }
}