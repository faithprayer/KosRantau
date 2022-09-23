package com.example.login.room

import android.provider.ContactsContract
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val Username:String,
    val NomorHandphone:String,
    val Email:String,
    val TanggalLahir:String,
    val Password:String
)