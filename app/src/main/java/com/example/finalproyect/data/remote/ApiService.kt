package com.example.finalproyect.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    companion object {
        const val BASE_URL = "http://192.168.100.7:8080"
    }
}