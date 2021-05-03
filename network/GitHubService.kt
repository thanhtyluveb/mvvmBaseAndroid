package com.example.thefirstprojecttdtdemo.network

import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubService : BaseService{
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String?)
}
