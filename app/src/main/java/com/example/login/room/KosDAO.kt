package com.example.login.room

import androidx.room.*

    @Dao
    interface KosDAO {
        @Insert
         suspend fun addKos(kos: Kos)

        @Update
         suspend fun updateKos(kos: Kos)

        @Delete
         suspend fun deleteKos(kos: Kos)

        @Query("SELECT * FROM kos")
         suspend fun getKos() : List<Kos>

        @Query("SELECT * FROM kos WHERE id =:kos_id")
        suspend fun getKos(kos_id:Int) : List<Kos>

    }
