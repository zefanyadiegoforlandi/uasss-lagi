package com.ppb.travellin.services.model

import com.google.firebase.firestore.FieldValue

data class User(
    var id : String = "",
    var username : String = "",
    var email : String = "",
    var nim : String = "",
    var password : String = "",
    var role : String = "Role_User",
    var user_auth_status : String = "log_out",
    val createdAt : Any? = FieldValue.serverTimestamp(),
    var updatedAt : Any? = FieldValue.serverTimestamp()
)