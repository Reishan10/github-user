package com.dicoding.github.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.github.data.local.UserDatabase
import com.dicoding.github.data.local.UserFavorite
import com.dicoding.github.data.local.UserFavoriteDao

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao: UserFavoriteDao?
    private var userDB: UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDao = userDB?.userFavoriteDao()
    }

    fun getUserFavorite(): LiveData<List<UserFavorite>>? {
        return userDao?.getUserFavorite()
    }
}