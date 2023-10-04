package com.dicoding.github.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.github.api.RetrofitClient
import com.dicoding.github.data.model.User
import com.dicoding.github.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<User>>()
    private val adapter = UserAdapter()

    fun setSearchUser(query: String){
        RetrofitClient.apiInstance
            .getSearchUsers(query)
            .enqueue(object: Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body()?.items)
                        Log.d("UserViewModel", "Data berhasil diterima: ${response.body()?.items}")
                        adapter.setList(response.body()?.items ?: ArrayList())
                    } else {
                        Log.e("UserViewModel", "Gagal mengambil data: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                   Log.d("Fuilure", t.message.toString())
                }

            })
    }

    fun getSearchUser(): LiveData<ArrayList<User>>{
        return listUsers;
    }
}