package com.dicoding.github.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserFavoriteDao {
    @Insert
    suspend fun addToFavorite(userFavorite: UserFavorite)

    @Query("SELECT * FROM user_favorite")
    fun getUserFavorite(): LiveData<List<UserFavorite>>

    @Query("SELECT COUNT(*) FROM user_favorite WHERE user_favorite.id = :id")
    fun checkUser(id: Int): Int

    @Query("DELETE FROM user_favorite WHERE user_favorite.id = :id")
    fun removeFavorite(id: Int): Int
}
