package com.example.livetolive

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class bottomSheetSleep : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())


        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottomsheet_sleep, null)

        val txtGoal = view.findViewById<EditText>(R.id.editObjetivoSleep)
        val btnSave = view.findViewById<Button>(R.id.btnGuardarObjetivoSleep)


        val objetivoActual = sharedPreferencesApp.getInt("SleepGoal", 8)
        txtGoal.setText(objetivoActual.toString())


        btnSave.setOnClickListener {
            val nuevoValor = txtGoal.text.toString().toIntOrNull()

            if (nuevoValor != null && nuevoValor > 0) {


                sharedPreferencesApp.saveInt("SleepGoal", nuevoValor)


                parentFragmentManager.setFragmentResult("sleep_refresh", Bundle())

                dismiss()
            }
        }

        dialog.setContentView(view)
        return dialog
    }
}
