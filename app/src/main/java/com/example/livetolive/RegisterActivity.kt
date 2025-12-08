package com.example.livetolive

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.roundToInt

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        //Extraccion de los datos de la vista (Inge si ve esto no es chat gpt, soy yo que ahora documento codigo pq asi me acuerdo mas facil)
        val btnSig=findViewById<Button>(R.id.btnSiguiente)
        val etxNombre=findViewById<EditText>(R.id.etxNombre)
        val etxPeso=findViewById<EditText>(R.id.etxPeso)
        val etxAltura=findViewById<EditText>(R.id.etxAltura)
        val etxEdad=findViewById<EditText>(R.id.etxEdad)
        val hombre=findViewById<RadioButton>(R.id.rdHombre)
        val mujer=findViewById<RadioButton>(R.id.rdMujer)

        //Variables para formula de hidratacion
        var totalHidra:Float=0f
        var Fhidratacion: Float=0f
        var Asexo:Float=0f
        var Aedad:Float=0f

        //Variables para formula de Pasos para actividad fisica
        var Ppeso:Int=0
        var Paltura:Int=0
        var Pedad:Int=0
        var Psexo:Int=0
        var FActividad: Int=0








        btnSig.setOnClickListener {
            if (etxNombre.text.toString().isNotEmpty() && etxPeso.text.toString().isNotEmpty() && etxAltura.text.toString().isNotEmpty() && etxEdad.text.toString().isNotEmpty() && (hombre.isChecked || mujer.isChecked)) {
                sharedPreferencesApp.saveString("Nombre", etxNombre.text.toString())
                sharedPreferencesApp.saveFloat("Peso", etxPeso.text.toString().toFloat())
                sharedPreferencesApp.saveFloat("Altura", etxAltura.text.toString().toFloat())
                sharedPreferencesApp.saveInt("Edad", etxEdad.text.toString().toInt())

                Ppeso=8*(etxPeso.text.toString().toInt()-70)
                Paltura=15*((etxAltura.text.toString().toInt())-170)
                if (hombre.isChecked) {
                    sharedPreferencesApp.saveString("Sexo", "Hombre")
                    Asexo=0.5f
                    Psexo=-300
                } else {
                    sharedPreferencesApp.saveString("Sexo", "Mujer")
                    Asexo=0f
                    Psexo=0
                }
                if(etxEdad.text.toString().toInt()>30){
                    Aedad=0.005f*(etxEdad.text.toString().toInt().toFloat()-30f)
                    Pedad=20*(etxEdad.text.toString().toInt()-30)
                }else{
                    Aedad=0f
                    Pedad=0
                }
                totalHidra=0.033f*(etxPeso.text.toString().toFloat())+(0.001f*(etxAltura.text.toString().toFloat()))+Asexo+Aedad
                Fhidratacion=((totalHidra * 10).roundToInt() / 10f)
                FActividad=(6000+Ppeso+Paltura+Psexo-Pedad)
                sharedPreferencesApp.saveFloat("HidrateGoal",Fhidratacion)
                sharedPreferencesApp.saveInt("ActividadGoal",FActividad)
                sharedPreferencesApp.saveInt("SleepGoal",8)
                startActivity(Intent(this, PlanActivity::class.java))
            }else{
                Toast.makeText(this,"Debes llenar todos los campos",Toast.LENGTH_SHORT).show()
            }

        }


    }
}