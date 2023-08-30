package com.saverone.exercise.data

import com.saverone.exercise.api.TmdbAPI
import com.saverone.exercise.model.FavoriteRequest
import com.saverone.exercise.model.MoviesWrapper
import com.saverone.exercise.model.ResponseWrapper
import retrofit2.Call
import javax.inject.Inject


class RemoteDataSource@Inject constructor(val api: TmdbAPI){

    enum class MovieFilterType {
        popular,
        currently_played,
        favorites,
    }

    companion object {
        val API_KEY = "2c46288716a18fb7aadcc2a801f3fc6b"

        //My key
//        val API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwN2FkNGI3Y2Y1OTJkMjkyM2ZiZGMzNGYzOTMzYWFkMyIsInN1YiI6IjY0ZWQ4ZDBjNTI1OGFlMDBhZGQ2MTYwMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.GoNLSnhrEGIEvtOJ-FKyfUnuniKyFGFNn-bS_NdtyW0"

        val API_KEY_HEADER = "Bearer " + API_KEY
        val BASE_IMAGES_URL = "https://image.tmdb.org/t/p/original/"
    }

    fun geMovies(page:Int, filterType: MovieFilterType):Call<MoviesWrapper?>{
        return when(filterType){
            MovieFilterType.popular ->
                      api.getPopular(page, API_KEY_HEADER);
            MovieFilterType.currently_played ->
                api.getCurrentlyBroadcasted(page, API_KEY_HEADER);
            else->
                api.getFavoriteMovies(page, API_KEY_HEADER);

        }
    }

    fun setFavorite(movieID:Int):Call<ResponseWrapper?>{
       return  api.addToFavorites(FavoriteRequest(movieID), API_KEY_HEADER)
    }



}