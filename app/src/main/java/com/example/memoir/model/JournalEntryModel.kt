package com.example.memoir.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JournalEntryModel(
    var id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val date: String = ""
): Parcelable
