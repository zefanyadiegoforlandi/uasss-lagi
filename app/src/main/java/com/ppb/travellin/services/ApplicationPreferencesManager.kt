package com.ppb.travellin.services

import android.content.Context
import android.icu.util.Calendar

class ApplicationPreferencesManager(context : Context) {
    private val sharedPreferences = context.getSharedPreferences("account_data", Context.MODE_PRIVATE)
    private val SPID : String = "username_id"
    private val SPROLE : String = "user_role"
    private val SPNAME : String = "username_name"
    private val SPN : String = "username_nim"
    private val SPIMAGE : String = "profile_image"

    val usernameId: String?
        get() = sharedPreferences.getString(SPID, null)
    val isUserIdNotNull : Boolean
        get() = sharedPreferences.getString(SPID, null) != null

    val isUserAdmin : Boolean
        get() = sharedPreferences.getString(SPROLE, null) == "Role_Admin"

    val usernameName: String?
        get() = sharedPreferences.getString(SPNAME, null)

    val usernameNim: String?
        get() = sharedPreferences.getString(SPN, null)

    val profileImage: String?
        get() = sharedPreferences.getString(SPIMAGE, null)

    fun saveProfileImage(imageUri: String) {
        with(sharedPreferences.edit()) {
            putString(SPIMAGE, imageUri)
            apply()
        }
    }

    fun saveUsernameId(usernameId: String, role: String) {
        val expirationDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 5)
        }.timeInMillis

        with(sharedPreferences.edit()) {
            putString(SPID, usernameId)
            putString(SPROLE, role)
            putLong("username_expiration_date", expirationDate)
            apply()
        }
    }

    fun saveUserData(name: String, nim : String) {
        val expirationDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 6)
        }.timeInMillis

        with(sharedPreferences.edit()) {
            putString(SPNAME, name)
            putString(SPN, nim)
            putLong("uname_expiration_date", expirationDate)
            apply()
        }
    }

    fun deleteRole() {
        with(sharedPreferences.edit()) {
            remove(SPROLE)
            apply()
        }
    }

    fun deleteUsername() {
        with(sharedPreferences.edit()) {
            remove(SPID)
            remove(SPROLE)
            remove(SPNAME)
            remove(SPN)
            remove(SPIMAGE)
            apply()
        }
    }


}