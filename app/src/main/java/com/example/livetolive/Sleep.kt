package com.example.livetolive

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Sleep")
data class Sleep(
    @PrimaryKey(autoGenerate = true) val idSleep: Int = 0,
    val horasObjetivo: Float,
    val horasRegistradas: Float,
    val fecha: Date
)
