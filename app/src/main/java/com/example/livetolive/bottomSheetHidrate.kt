package com.example.livetolive

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.livetolive.databinding.FragmentBottomSheetHidrateBinding
import com.example.livetolive.databinding.FragmentHidrateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [bottomSheetHidrate.newInstance] factory method to
 * create an instance of this fragment.
 */
class bottomSheetHidrate : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetHidrateBinding


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_bottom_sheet_hidrate, container, false)
        val btnEditar=view.findViewById<Button>(R.id.btnEditar)
        val objetivo=view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etxObjetivo)

        val dialogView=layoutInflater.inflate(R.layout.alertpopup,null)
        val btnDialog=dialogView.findViewById<Button>(R.id.btnEntendido)
        val infoPopup=dialogView.findViewById<TextView>(R.id.txtAviso)
        infoPopup.text="Tomar mas de 6 Litros de agua al dia puede provocar problemas para la salud"
        val dialog= AlertDialog.Builder(requireContext()).setView(dialogView).create()
        objetivo.setText(sharedPreferencesApp.getFloat("HidrateGoal").toString())

        btnEditar.setOnClickListener {
            if (objetivo.text.toString().isNotEmpty()) {
                if(objetivo.text.toString().toFloat()>=6){
                    dialog.show()
                    btnDialog.setOnClickListener {
                        dialog.dismiss()
                    }
                    objetivo.setText(sharedPreferencesApp.getFloat("HidrateGoal").toString())
                }else{
                    sharedPreferencesApp.saveFloat("HidrateGoal",objetivo.text.toString().toFloat())
                    parentFragmentManager.setFragmentResult(
                        "hidra_refresh",
                        Bundle().apply { putBoolean("updated", true) }
                    )

                    dismiss()
                }
            }else{
                Toast.makeText(requireContext(), "Por favor ingrese un objetivo", Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment bottomSheetHidrate.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            bottomSheetHidrate().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

