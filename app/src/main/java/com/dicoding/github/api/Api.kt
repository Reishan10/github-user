package com.dicoding.github.api

import com.dicoding.github.data.model.DetailUserResponse
import com.dicoding.github.data.model.User
import com.dicoding.github.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}