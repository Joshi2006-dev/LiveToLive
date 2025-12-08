package com.example.livetolive

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Actividad")
data class Actividad(
    @PrimaryKey(autoGenerate = true) val idActividad: Int = 0,
    val pasosObjetivo: Int,
    val pasosRegistrados: Int,
    val fecha: Date
)
