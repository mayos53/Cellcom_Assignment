package com.saverone.exercise.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saverone.exercise.data.RemoteDataSource
import com.saverone.exercise.model.MoviesWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel@Inject constructor(private val dataSource: RemoteDataSource): ViewModel() {

    var filterType: RemoteDataSource.MovieFilterType = RemoteDataSource.MovieFilterType.popular
    var page = 0
    var moviesLiveData = MutableLiveData<MoviesWrapper?>()


    fun applyFilterType(filterType: RemoteDataSource.MovieFilterType) {
        if (filterType != this.filterType) {
            this.filterType = filterType;
            fetchMovies(true);
        }
    }

    fun fetchMovies(firstPage: Boolean) {
        page = if (firstPage) 1 else page + 1
        val call = dataSource.geMovies(page, filterType)
        call.enqueue(object : Callback<MoviesWrapper?> {
            override fun onResponse(call: Call<MoviesWrapper?>, response: Response<MoviesWrapper?>) {
                if (response.isSuccessful()) {
                    moviesLiveData.value = response.body()
                } else {
                    moviesLiveData.value = null
                }
            }

            override fun onFailure(call: Call<MoviesWrapper?>, t: Throwable) {
                moviesLiveData.value = null
            }
        })


    }
}






//    private fun fetchPhotos(): ArrayList<String> {
//        return repo.getPopular(1)
//    }




