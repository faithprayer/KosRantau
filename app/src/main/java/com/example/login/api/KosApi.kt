package com.example.login.api

class KosApi {
    companion object {
        //sesuaikan ipv4 masing-masing
        val BASE_URL = "https://kosrantau.my.id/api/"

        val GET_ALL_URL = BASE_URL + "kos/"
        val GET_BY_ID_URL = BASE_URL + "kos/"
        val ADD_URL = BASE_URL + "kos"
        val UPDATE_URL = BASE_URL + "kos/"
        val DELETE_URL = BASE_URL + "kos/"
    }
}