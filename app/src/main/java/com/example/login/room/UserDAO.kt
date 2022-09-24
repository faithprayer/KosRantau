package com.example.login.room

import androidx.room.*


@Dao
interface UserDAO {
    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM user")
     fun getUsers() : List<User>

    @Query("SELECT * FROM user WHERE id =:user_id")
     fun getUser(user_id:Int) : User

     @Query("SELECT * FROM user WHERE username = :username AND password = :password")
     fun cekUser(username: String, password: String): User?
}