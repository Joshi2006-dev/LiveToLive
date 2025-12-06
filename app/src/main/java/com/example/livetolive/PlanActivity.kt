package com.example.livetolive

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plan)
        val btnFinalizar=findViewById<Button>(R.id.btnFinalizar)
        val btnVolver=findViewById<ImageView>(R.id.btnVolver)
        btnFinalizar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        btnVolver.setOnClickListener {
            finish()
        }


    }
}