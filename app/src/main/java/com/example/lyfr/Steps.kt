package com.example.lyfr

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Steps(
    @PrimaryKey(autoGenerate = true) var id  : Int,
    var steps : Int,
    var date : String
)