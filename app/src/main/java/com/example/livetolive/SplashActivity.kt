package com.example.livetolive

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class SplashActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        db = AppDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {

            val objetivo = 2.5f

            // Creamos un map de día -> litros registrados
            val diasYProgreso = mapOf(
                9 to 2.5f,   // completo
                10 to 2.0f,  // incompleto
                11 to 2.5f,  // completo
                12 to 1.5f,  // incompleto
                13 to 2.5f,  // completo
                14 to 2.3f,  // incompleto
                15 to 2.5f   // completo
            )

            diasYProgreso.forEach { (dia, litrosRegistrados) ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, 2025)
                cal.set(Calendar.MONTH, Calendar.DECEMBER)
                cal.set(Calendar.DAY_OF_MONTH, dia)
                cal.set(Calendar.HOUR_OF_DAY, 12) // mediodía, solo por consistencia
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                val hidratacion = Hidratacion(
                    litrosObjetivo = objetivo,
                    litrosRegistrados = litrosRegistrados,
                    fecha = cal.time
                )

                db.hidratacionDao().insert(hidratacion)
            }
        }

        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (sharedPreferencesApp.getString("Nombre")=="") {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, MainActivity::class.java))
            }

        },3800)
    }
}