package com.saverone.exercise.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saverone.exercise.data.RemoteDataSource
import com.saverone.exercise.model.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieInfoViewModel@Inject constructor(private val dataSource: RemoteDataSource): ViewModel() {

    var favoriteStateLiveData = MutableLiveData<Boolean?>()
   fun setFavorite(movieID:Int){
       val call = dataSource.setFavorite(movieID)
       call.enqueue(object:Callback<ResponseWrapper?>{
           override fun onResponse(call: Call<ResponseWrapper?>, response: Response<ResponseWrapper?>) {
               if (response.isSuccessful()) {
                   favoriteStateLiveData.value = response.body()?.success
               }else{
                   favoriteStateLiveData.value = null
               }
           }

           override fun onFailure(call: Call<ResponseWrapper?>, t: Throwable) {
               favoriteStateLiveData.value = null
           }
       })
   }


}




//    private fun fetchPhotos(): ArrayList<String> {
//        return repo.getPopular(1)
//    }




