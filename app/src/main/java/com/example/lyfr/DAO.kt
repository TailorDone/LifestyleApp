package com.example.lyfr

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAO {
    @Query("SELECT * FROM User limit 1")
    fun getUser(): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User)

//    @Query("SELECT Count(*) FROM User")
//    fun getNumUsers() : Int

//    @Query("SELECT * FROM User")
//    fun getAllUsers(): List<LiveData<User>>

//    @Query("DELETE FROM com.example.lyfr.User")
//    suspend fun deleteUser(user: com.example.lyfr.User)


}