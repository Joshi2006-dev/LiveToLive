package com.example.livetolive

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object sharedPreferencesApp {
    private const val PREF_NAME="LiveToLivePrefs"
    private const val MODE= Context.MODE_PRIVATE
    private lateinit var prefs: SharedPreferences

    fun init(context: Context){
        prefs=context.getSharedPreferences(PREF_NAME,MODE)
        setDefaultValues()
    }

    private fun setDefaultValues(){
        val alreadyInitialized=prefs.getBoolean("Init",false)
        val hoy = System.currentTimeMillis()
        if(!alreadyInitialized){
            prefs.edit().apply{
                //shared Preferences para los estados de los logros
                putBoolean("BronzPop",false)
                putBoolean("SilverPop",false)
                putBoolean("GoldPop",false)
                putBoolean("PlatinumPop",false)
                putBoolean("DiamondPop",false)
                putBoolean("KingPop",false)
                //Para los objetivos cumplidos de cada seccion
                putBoolean("pasosCumplidos",false)
                putBoolean("litrosTomados",false)
                putBoolean("horasCumplidas",false)

                //contador de logros
                putInt("logrosObtenido",0)

                //Este de aqui hace que solo se ejecute una vez
                putBoolean("Init",true)

                //shared Preferences para los objetivos y progresos de hidratacion
                putFloat("HidrateGoal",0f)
                putFloat("HidratationProgress",0f)

                //shared Preferences para los objetivos y progresos de actividad fisica
                putInt("ActividadGoal",0)
                putInt("ActividadProgress",0)
                putInt("racha", 0)
                putBoolean("sumadoHoy", false)
                putString("ultimaFecha", "")
                putInt("objetivoPasos", 3000)
                putFloat("objetivoDistancia", 3.0f)
                putFloat("objetivoCalorias", 200.0f)

                //shared Preferences para los objetivos y progresos de sueño
                putInt("SleepGoal",0)
                putInt("SleepProgress",0)

                //shared Preferences para los datos personales del usuario
                putString("Nombre","")
                putFloat("Peso",0f)
                putFloat("Altura",0f)
                putInt("Edad",0)
                putString("Sexo","")

                //shared Preferences para hacer la comparacion de dias y disparar insercciones automaticas
                putLong("LastDayTimestamp", hoy)
                apply()
            }
        }
    }

    fun saveLastDay(timestamp: Long) {
        prefs.edit { putLong("LastDayTimestamp", timestamp) }
    }

    fun getLastDay(): Long {
        return prefs.getLong("LastDayTimestamp", 0L)
    }
    fun saveBoolean(key: String, value: Boolean){
        prefs.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String, default: Boolean=false): Boolean{
        return prefs.getBoolean(key,default)
    }

    fun saveFloat(key: String, value: Float){
        prefs.edit { putFloat(key, value) }
    }
    fun getFloat(key: String, default: Float=0f): Float{
        return prefs.getFloat(key,default)
    }

    fun saveString(key: String, value: String) {
        prefs.edit { putString(key, value) }
    }

    fun getString(key: String, default: String=""): String? {
        return prefs.getString(key, default)
    }
    fun saveInt(key: String, value: Int){
        prefs.edit { putInt(key, value) }
    }
    fun getInt(key: String, default: Int=0): Int{
        return prefs.getInt(key,default)
    }


}