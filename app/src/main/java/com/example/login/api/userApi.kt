package com.example.login.api

class userApi {

    companion object{
        //sesuaikan ipv4 masing-masing
        val BASE_URL = "http://192.168.100.82/TUBES_PBP_API/public/"

        val GET_BY_ID_URL = BASE_URL + "user/"
        val UPDATE_URL = BASE_URL + "user/"
        val DELETE_URL = BASE_URL + "user/"

        val ADD = BASE_URL + "user"
        val LOGIN = BASE_URL + "checklogin"
    }
}