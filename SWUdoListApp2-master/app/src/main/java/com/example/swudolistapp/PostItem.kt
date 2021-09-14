package com.example.swudolistapp

import com.google.gson.annotations.SerializedName
import java.util.*

// DATA 형태 정의
data class PostItem (
    val code: String,
    val msg: String
)

data class GetInfo(
    val subjects: String,
    val email: String,
)

data class User(
    var id: String? = null,
    var pw: String? = null,
    var email: String? = null,
    var subjects: String? = null,
)

data class EditItem(
    val created: String,
)