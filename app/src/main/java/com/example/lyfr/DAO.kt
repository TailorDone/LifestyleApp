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

    @Query("SELECT * FROM Steps")
    fun getSteps(): Flow<Steps>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: Steps)

    @Query("SELECT id, steps, date FROM Steps WHERE date = :date")
    fun getTodaysSteps(date: String): Flow<Steps>

    @Query("SELECT SUM(steps) AS steps from Steps")
    fun getTotalSteps(): Flow<Int>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSteps(steps: Steps)

    @Query("SELECT COUNT(*) FROM Steps")
    fun getStepRowCount(): Flow<Int>

    @Query("SELECT * FROM Steps ORDER BY id DESC LIMIT 1")
    fun getMostRecentDay(): Flow<Steps>

}