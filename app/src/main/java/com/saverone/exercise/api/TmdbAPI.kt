package com.saverone.exercise.api

import com.saverone.exercise.model.FavoriteRequest
import com.saverone.exercise.model.MoviesWrapper
import com.saverone.exercise.model.ResponseWrapper
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TmdbAPI {



    @GET("movie/popular")
     fun getPopular(
        @Query("page") page: Int,
        @Header("Authorization") api_key: String
    ): Call<MoviesWrapper?>

    @GET("movie/now_playing")
    fun getCurrentlyBroadcasted(
        @Query("page") page: Int,
        @Header("Authorization") api_key: String
    ): Call<MoviesWrapper?>

    @GET("account/1/favorite/movies")
    fun getFavoriteMovies(
        @Query("page") page: Int,
        @Header("Authorization") api_key: String
    ): Call<MoviesWrapper?>

    @POST("account/1/favorite")
    fun addToFavorites(
        @Body requestBody:FavoriteRequest,
        @Header("Authorization") api_key: String,
    ): Call<ResponseWrapper?>
}