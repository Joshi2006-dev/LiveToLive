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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val peso=profile.findViewById<TextView>(R.id.txtPeso)
        val altura=profile.findViewById<TextView>(R.id.txtAltura)
        val edad=profile.findViewById<TextView>(R.id.txtEdad)
        val sexo=profile.findViewById<TextView>(R.id.txtSexo)
        val nombre=profile.findViewById<TextView>(R.id.txtNombreUsuario)
        val imc=profile.findViewById<TextView>(R.id.txtIMC)
        val indicador=profile.findViewById<TextView>(R.id.txtIMCindicator)

        peso.text=sharedPreferencesApp.getFloat("Peso").toString()+" Kg"
        altura.text=(sharedPreferencesApp.getFloat("Altura")/100).toString()+" m"
        edad.text=sharedPreferencesApp.getInt("Edad").toString()+" años"
        sexo.text=sharedPreferencesApp.getString("Sexo")
        nombre.text=sharedPreferencesApp.getString("Nombre")
        val pesocalc = sharedPreferencesApp.getFloat("Peso", 0f)
        val alturaCm = sharedPreferencesApp.getFloat("Altura", 0f)
        val alturaM = alturaCm / 100
        val IMCcalc: Float = pesocalc / (alturaM * alturaM)
        val IMCRedondeado = round(IMCcalc * 10) / 10
        imc.text=IMCRedondeado.toString()

        if (IMCRedondeado < 18.5) {
            indicador.text = "PESO BAJO"
        }else if (IMCRedondeado in 18.5..24.9) {
            indicador.text = "PESO NORMAL"
        }else if (IMCRedondeado in 25.0..29.9) {
            indicador.text = "SOBREPESO"
        }else if (IMCRedondeado in 30.0..34.9) {
            indicador.text = "OBESIDAD TIPO I"
        }else if (IMCRedondeado in 35.0..39.9) {
            indicador.text = "OBESIDAD TIPO II"
        }else if (IMCRedondeado >= 40) {
            indicador.text = "OBESIDAD TIPO III y IV"
        }

        val btnEditInfo = profile.findViewById<ImageView>(R.id.btnEditInfo)
        btnEditInfo.setOnClickListener {
            val bottomSheet = ProfileText()
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
        return profile
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