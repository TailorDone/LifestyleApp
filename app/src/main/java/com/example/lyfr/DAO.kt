package com.example.lyfr

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAO {
    @Query("SELECT * FROM User")
    fun getUsers(): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

//    @Query("DELETE FROM com.example.lyfr.User")
//    suspend fun deleteUser(user: com.example.lyfr.User)


}