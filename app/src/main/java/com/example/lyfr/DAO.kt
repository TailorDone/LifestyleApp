package com.example.lyfr

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.Update

@Dao
interface DAO {
    @Query("SELECT * FROM User limit 1")
    fun getUser(): Flow<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User)

    @Query("UPDATE User SET lifestyle=:lifestyle, weightChangeGoal=:weightChangeGoal, weightGoalOption=:weightGoalOption WHERE name = :name")
    suspend fun updateFitnessGoals(lifestyle: Int, weightChangeGoal: Double, weightGoalOption: Int, name: String)
}