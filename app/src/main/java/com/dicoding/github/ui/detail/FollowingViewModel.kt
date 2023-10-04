package com.dicoding.github.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.github.api.RetrofitClient
import com.dicoding.github.data.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {
    val listFollowing = MutableLiveData<ArrayList<User>>()

    fun setListFollowing(username: String){
        RetrofitClient.apiInstance
            .getFollowing(username)
            .enqueue(object: Callback<ArrayList<User>>{
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful){
                        val FollowingData = response.body()
                        listFollowing.postValue(FollowingData)

                        if (FollowingData != null && FollowingData.isNotEmpty()) {
                            Log.d("FollowingViewModel", "Data pengikut berhasil diambil.")
                        } else {
                            Log.d("FollowingViewModel", "Data pengikut kosong.")
                        }
                    } else {
                        Log.e("FollowingViewModel", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun getListFollowing(): LiveData<ArrayList<User>>{
        return listFollowing
    }
}