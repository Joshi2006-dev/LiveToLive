package com.example.livetolive

import androidx.lifecycle.ViewModel

class ProfileTextViewModel : ViewModel() {
    var peso: Float = 0f
    var altura: Float = 0f
    var edad: Int = 0
    var sexo: String = ""

    fun cargarDatos() {
    }

    fun guardarDatos() {
    }
}