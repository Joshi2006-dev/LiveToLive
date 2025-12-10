package com.example.livetolive

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class AlarmBottomSheet : BottomSheetDialogFragment() {

    private lateinit var timePicker: TimePicker
    private lateinit var btnGuardar: Button
    private lateinit var btnBorrar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.bottomsheet_alarm, container, false)

        timePicker = view.findViewById(R.id.timePicker)
        btnGuardar = view.findViewById(R.id.btnGuardarAlarma)
        btnBorrar = view.findViewById(R.id.btnBorrarAlarma)

        // ese boton es el que me guarda la alarma
        btnGuardar.setOnClickListener {

            val prefs = requireContext().getSharedPreferences("alarm", Context.MODE_PRIVATE)
            val alarmaActiva = prefs.getBoolean("alarma_activa", false)

            if (alarmaActiva) {
                Toast.makeText(requireContext(), "Ya tienes una alarma configurada", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val hour = timePicker.hour
            val minute = timePicker.minute

            programarAlarma(hour, minute)

            prefs.edit().putBoolean("alarma_activa", true).apply()

            guardarHoraAcostado()

            dismiss()
        }

        // este elimina la alrama por si se equivoco el mongolon
        btnBorrar.setOnClickListener {
            cancelarAlarma()
        }

        return view
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun programarAlarma(hora: Int, minuto: Int) {
        val context = requireContext()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hora)
        calendar.set(Calendar.MINUTE, minuto)
        calendar.set(Calendar.SECOND, 0)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pending
        )

        Toast.makeText(context, "Alarma configurada", Toast.LENGTH_SHORT).show()
    }

    private fun cancelarAlarma() {
        val context = requireContext()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pending)

        // Quitar bandera de alarma activa
        val prefs = context.getSharedPreferences("alarm", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("alarma_activa", false).apply()

        Toast.makeText(context, "Alarma eliminada", Toast.LENGTH_SHORT).show()

        dismiss()
    }

    private fun guardarHoraAcostado() {
        val prefs = requireContext().getSharedPreferences("sleep", Context.MODE_PRIVATE)
        prefs.edit().putLong("hora_acostado", System.currentTimeMillis()).apply()
    }
}
