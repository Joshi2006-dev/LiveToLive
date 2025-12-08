package com.example.livetolive

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plan)
        val btnFinalizar=findViewById<Button>(R.id.btnFinalizar)
        val btnVolver=findViewById<ImageView>(R.id.btnVolver)
        val txtobjetivodiarioHidra=findViewById<TextView>(R.id.txtobjetivodiarioHidra)
        val txtobjetivodiarioActividad=findViewById<TextView>(R.id.txtobjetivodiarioActividad)
        val txtobjetivosleep=findViewById<TextView>(R.id.txtobjetivosleep)

        txtobjetivodiarioHidra.text=sharedPreferencesApp.getFloat("HidrateGoal").toString()+" Litros de Agua al día"
        txtobjetivodiarioActividad.text=sharedPreferencesApp.getInt("ActividadGoal").toString()+" Pasos al día"
        txtobjetivosleep.text=sharedPreferencesApp.getInt("SleepGoal").toString()+" Horas de Sueño al día"

        btnFinalizar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        btnVolver.setOnClickListener {
            finish()
        }


    }
}