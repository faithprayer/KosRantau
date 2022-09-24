package com.example.login.room

import androidx.room.*


@Dao
interface UserDAO {
    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user")
     fun getUsers() : List<User>

    @Query("SELECT * FROM user WHERE id =:user_id")
     fun getUser (user_id:Int) : List<User>
}