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
        if(!alreadyInitialized){
            prefs.edit().apply{
                putBoolean("BronzPop",false)
                putBoolean("SilverPop",false)
                putBoolean("GoldPop",false)
                putBoolean("PlatinumPop",false)
                putBoolean("DiamondPop",false)
                putBoolean("KingPop",false)
                putBoolean("Init",true)
                putFloat("HidrateGoal",0f)
                putFloat("HidratationProgress",0f)
                apply()
            }
        }
    }


    //Aqui van los SharedPreferences para Achievements (Yo ando comentando para que se entienda, no es que chat GPT me anda haciendo el ejercicio, se me ubican)
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


}