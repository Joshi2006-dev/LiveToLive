package com.example.livetolive

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Hidratacion")
data class Hidratacion(
    @PrimaryKey(autoGenerate = true) val idHidratacion: Int = 0,
    val litrosObjetivo: Float,
    val litrosRegistrados: Float,
    val fecha: Date
)
