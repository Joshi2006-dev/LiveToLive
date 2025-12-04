package com.example.livetolive

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class AlarmBottomSheet : BottomSheetDialogFragment() {

    private lateinit var timePicker: TimePicker
    private lateinit var btnGuardar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.bottomsheet_alarm, container, false)

        timePicker = view.findViewById(R.id.timePicker)
        btnGuardar = view.findViewById(R.id.btnGuardarAlarma)

        btnGuardar.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            programarAlarma(hour, minute)
            guardarHoraAcostado()

            dismiss()
        }

        return view
    }

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
    }

    private fun guardarHoraAcostado() {
        val prefs = requireContext().getSharedPreferences("sleep", Context.MODE_PRIVATE)
        prefs.edit().putLong("hora_acostado", System.currentTimeMillis()).apply()
    }
}
