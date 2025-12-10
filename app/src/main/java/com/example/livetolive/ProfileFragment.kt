package com.example.livetolive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import kotlin.math.round

interface ProfileUpdateCallback {
    fun onProfileDataUpdated()
}

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), ProfileUpdateCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var peso: TextView
    private lateinit var altura: TextView
    private lateinit var edad: TextView
    private lateinit var sexo: TextView
    private lateinit var nombre: TextView
    private lateinit var imc: TextView
    private lateinit var indicador: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val profile= inflater.inflate(R.layout.fragment_profile, container, false)
        val scrollview=profile.findViewById<ScrollView>(R.id.scroller)
        val btnVolver=profile.findViewById<ImageView>(R.id.btnVolver)

        peso = profile.findViewById(R.id.txtPeso)
        altura = profile.findViewById(R.id.txtAltura)
        edad = profile.findViewById(R.id.txtEdad)
        sexo = profile.findViewById(R.id.txtSexo)
        nombre = profile.findViewById(R.id.txtNombreUsuario)
        imc = profile.findViewById(R.id.txtIMC)
        indicador = profile.findViewById(R.id.txtIMCindicator)

        loadProfileData()

        val btnEditInfo = profile.findViewById<ImageView>(R.id.btnEditInfo)
        btnEditInfo.setOnClickListener {
            val bottomSheet = ProfileText(this)
            bottomSheet.show(parentFragmentManager, "ProfileText")
        }

        scrollview.apply {
            translationY = 100f
            alpha = 0f

            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500)
                .start()
        }

        btnVolver.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return profile
    }

    private fun loadProfileData() {
        val pesocalc = sharedPreferencesApp.getFloat("Peso", 0f)
        val alturaCm = sharedPreferencesApp.getFloat("Altura", 0f)
        val alturaM = alturaCm / 100

        // Uso de String.format() con los valores correctos
        peso.text = String.format("%.1f Kg", pesocalc)
        altura.text = String.format("%.2f m", alturaM)
        edad.text = sharedPreferencesApp.getInt("Edad").toString() + " años"
        sexo.text = sharedPreferencesApp.getString("Sexo")
        nombre.text = sharedPreferencesApp.getString("Nombre")

        // Cálculo y clasificación del IMC
        var IMCRedondeado = 0f
        // Prevención de división por cero
        if (alturaM > 0) {
            val IMCcalc: Float = pesocalc / (alturaM * alturaM)
            IMCRedondeado = round(IMCcalc * 10) / 10
        }

        imc.text = IMCRedondeado.toString()

        when (IMCRedondeado) {
            in 0.0f..18.4f -> indicador.text = "PESO BAJO"
            in 18.5f..24.9f -> indicador.text = "PESO NORMAL"
            in 25.0f..29.9f -> indicador.text = "SOBREPESO"
            in 30.0f..34.9f -> indicador.text = "OBESIDAD TIPO I"
            in 35.0f..39.9f -> indicador.text = "OBESIDAD TIPO II"
            in 40.0f..Float.MAX_VALUE -> indicador.text = "OBESIDAD TIPO III y IV"
            else -> indicador.text = "DATO INVÁLIDO"
        }
    }

    override fun onProfileDataUpdated() {
        loadProfileData()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}