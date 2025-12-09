package com.example.livetolive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.viewModels

private var Any?.isDraggable: Any
    get() {
        TODO()
    }
    set(value) {}
private var Any?.peekHeight: Any
    get() {
        TODO()
    }
    set(value) {}
private val BottomSheetDialogFragment?.behavior: Any
    get() {
        TODO()
    }

class ProfileText : BottomSheetDialogFragment() {

    private val viewModel: ProfileTextViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile_text, container, false)

        val etxPeso = view.findViewById<EditText>(R.id.etxPeso)
        val etxAltura = view.findViewById<EditText>(R.id.etxAltura)
        val etxEdad = view.findViewById<EditText>(R.id.etxEdad)
        val etxSexo = view.findViewById<EditText>(R.id.etxSexo)
        val btnEditar = view.findViewById<Button>(R.id.btnEditar)

        etxPeso.setText(sharedPreferencesApp.getFloat("Peso", 0f).toString())
        etxAltura.setText(sharedPreferencesApp.getFloat("Altura", 0f).toString())
        etxEdad.setText(sharedPreferencesApp.getInt("Edad", 0).toString())
        etxSexo.setText(sharedPreferencesApp.getString("Sexo", ""))

        btnEditar.setOnClickListener {
            val peso = etxPeso.text.toString().toFloatOrNull() ?: 0f
            val altura = etxAltura.text.toString().toFloatOrNull() ?: 0f
            val edad = etxEdad.text.toString().toIntOrNull() ?: 0
            val sexo = etxSexo.text.toString()

            if (peso > 0 && altura > 0 && edad > 0 && sexo.isNotEmpty()) {
                sharedPreferencesApp.edit()
                    .putFloat("Peso", peso)
                    .putFloat("Altura", altura)
                    .putInt("Edad", edad)
                    .putString("Sexo", sexo)
                    .apply()

                Toast.makeText(requireContext(), "Datos actualizados", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val behavior = (dialog as? BottomSheetDialogFragment)?.behavior
        behavior?.peekHeight = (resources.displayMetrics.heightPixels / 2)
        behavior?.isDraggable = true
    }
}

private fun Unit.apply() {
    TODO("Not yet implemented")
}

private fun Unit.putString(string: String, sexo: String) {
    TODO("Not yet implemented")
}

private fun Unit.putInt(string: String, edad: Int) {
    TODO("Not yet implemented")
}

private fun Unit.putFloat(string: String, peso: Float) {
    TODO("Not yet implemented")
}

private fun sharedPreferencesApp.edit() {
    TODO("Not yet implemented")
}
