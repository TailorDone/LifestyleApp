package com.example.lyfr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface DAO {
    @Query("SELECT * FROM User LIMIT 1")
    fun getUser(): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

//    @Query("DELETE FROM com.example.lyfr.User")
//    suspend fun deleteUser(user: com.example.lyfr.User)


}