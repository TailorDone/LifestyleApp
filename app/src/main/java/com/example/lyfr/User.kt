package com.example.lyfr

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var name : String,
    var zip : String,
    var age : Int,
    var sex : String,
    var height : Double,
    var weight : Double,
    var profilePicturePath : String?,
    var lifestyle : Int,
    var weightChangeGoal : Double,
    var weightGoalOption : Int,
)