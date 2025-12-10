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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import android.app.Dialog

class ProfileText(private val callback: ProfileUpdateCallback) : BottomSheetDialogFragment() {

    private lateinit var etNombre: EditText
    private lateinit var etPeso: EditText
    private lateinit var etAltura: EditText
    private lateinit var etEdad: EditText
    private lateinit var etSexo: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile_text, container, false)

        etNombre = view.findViewById(R.id.etxNombre)
        etPeso = view.findViewById(R.id.etxPeso)
        etAltura = view.findViewById(R.id.etxAltura)
        etEdad = view.findViewById(R.id.etxEdad)
        etSexo = view.findViewById(R.id.etxSexo)
        val btnEditar = view.findViewById<Button>(R.id.btnEditar)

        etNombre.setText(sharedPreferencesApp.getString("Nombre", ""))
        etPeso.setText(sharedPreferencesApp.getFloat("Peso", 0f).toString())
        etAltura.setText(sharedPreferencesApp.getFloat("Altura", 0f).toString())
        etEdad.setText(sharedPreferencesApp.getInt("Edad", 0).toString())
        etSexo.setText(sharedPreferencesApp.getString("Sexo", ""))

        btnEditar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val peso = etPeso.text.toString().toFloatOrNull()
            val altura = etAltura.text.toString().toFloatOrNull()
            val edad = etEdad.text.toString().toIntOrNull()
            val sexo = etSexo.text.toString()

            if (peso == null || altura == null || edad == null || nombre.isBlank() || sexo.isBlank()) {
                Toast.makeText(context, "Por favor, completa todos los campos correctamente.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPreferencesApp.saveString("Nombre", nombre)
            sharedPreferencesApp.saveFloat("Peso", peso)
            sharedPreferencesApp.saveFloat("Altura", altura)
            sharedPreferencesApp.saveInt("Edad", edad)
            sharedPreferencesApp.saveString("Sexo", sexo)

            callback.onProfileDataUpdated()
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        // Este es el método estándar para controlar el tamaño y comportamiento del BottomSheet
        val dialog = dialog
        if (dialog != null) {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet)

                behavior.peekHeight = resources.displayMetrics.heightPixels * 3 / 4
                behavior.isDraggable = true
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
}
