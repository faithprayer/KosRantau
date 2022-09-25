package com.example.login.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Kos(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val namaKos:String,
    val namaPengguna:String,
    val tanggalPesan:String,
    val tanggalMasuk:String,
)