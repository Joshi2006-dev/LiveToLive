package com.example.livetolive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface ObjetivoCallback {
    fun onGoalSet(nuevoObjetivoPasos: Int)
}

class PhysicalGoalText(private val callback: ObjetivoCallback) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_physical_goal_text, container, false)

        val etxObjetivoPasos = view.findViewById<EditText>(R.id.etxObjetivoPasos)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmarObjetivo)

        val objetivoActual = sharedPreferencesApp.getInt("objetivoPasos", 3000)
        etxObjetivoPasos.setText(objetivoActual.toString())

        btnConfirmar.setOnClickListener {
            val pasosString = etxObjetivoPasos.text.toString()
            val pasos = pasosString.toIntOrNull()

            if (pasos != null && pasos > 5) {
                callback.onGoalSet(pasos)

                Toast.makeText(requireContext(), "Objetivo de pasos actualizado", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Introduce un valor válido (ej. 3000)", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
    }
}