package com.margaritaolivera.atleta.core.session

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private var _token: String? = null
    private var _fcmToken: String? = null

    fun saveToken(token: String) { _token = token }
    fun getToken(): String? = _token

    fun saveFcmToken(token: String) { _fcmToken = token }
    fun getFcmToken(): String? = _fcmToken

    fun logout() { _token = null }
}