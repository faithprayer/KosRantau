package com.example.login.api

class userApi {

    companion object{
        //sesuaikan ipv4 masing-masing
        val BASE_URL = "https://kosrantau.my.id/api/"

        val GET_BY_ID_URL = BASE_URL + "profilUser/"
        val UPDATE_USER = BASE_URL + "profilUser/"
        val ADD = BASE_URL + "register"
        val LOGIN = BASE_URL + "login"
    }
}