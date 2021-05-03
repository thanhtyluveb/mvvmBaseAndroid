package com.example.thefirstprojecttdtdemo.network

import retrofit2.Retrofit

object Retrofit {
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL_BASE)
        .build()
    inline fun <reified T> getService(): T = retrofit.create(T::class.java)
}