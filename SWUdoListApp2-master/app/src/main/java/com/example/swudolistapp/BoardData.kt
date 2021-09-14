package com.example.swudolistapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

data class SubjectListData(
    val subjectName: String,
    val subjectCode: String,
)

data class ToDoListData(
    val context: String,
    var checked: Boolean,
)

@Parcelize
data class BoardData(
    val author: String,
    val subject: String,
    val title: String,
    val context: String,
    val created: String,
): Parcelable