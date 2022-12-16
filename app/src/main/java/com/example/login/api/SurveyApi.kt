package com.example.login.api

class SurveyApi {
    companion object {
        val BASE_URL = "http://192.168.8.132/PBP_API_LARAVEL/public/api/"

        val GET_ALL_URL = BASE_URL + "survei/"
        val GET_BY_ID_URL = BASE_URL + "survei/"
        val ADD_URL = BASE_URL + "survei"
        val UPDATE_URL = BASE_URL + "survei/"
        val DELETE_URL = BASE_URL + "survei/"
    }
}