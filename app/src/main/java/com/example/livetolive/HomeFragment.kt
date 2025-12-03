package com.example.livetolive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat.animate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.R.attr.progressDrawable
import android.animation.ObjectAnimator
import android.widget.ScrollView
import android.widget.TextView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val home =inflater.inflate(R.layout.fragment_home, container, false)
        val carta1=home.findViewById<CardView>(R.id.card)
        val carta2=home.findViewById<CardView>(R.id.card2)
        val carta3=home.findViewById<CardView>(R.id.card3)
        val progre1=home.findViewById<ProgressBar>(R.id.progressBar)
        val progre2=home.findViewById<ProgressBar>(R.id.progressBar2)
        val progre3=home.findViewById<ProgressBar>(R.id.progressBar3)
        val txtprogre1=home.findViewById<TextView>(R.id.txtProgreso)
        val scrollview=home.findViewById<ScrollView>(R.id.scroller)


        //Carga los datos que estan en el sharedPreferences Dentro de las cardvius
        val porcentajeAgua= (sharedPreferencesApp.getFloat("HidratationProgress")/3.5f)*100
        txtprogre1.text=sharedPreferencesApp.getFloat("HidratationProgress").toString()+" L"
        val anBar= barAnimation()

        anBar.animateProgress(progre1,0,porcentajeAgua.toInt())
        anBar.animateProgress(progre2,0,50)
        anBar.animateProgress(progre3,0,33)

        scrollview.apply {
            translationY = 100f
            alpha = 0f

            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500)
                .start()
        }
        carta1.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().
                    replace(R.id.Frame, HidrateFragment()).
                    addToBackStack(null).commit()
        }

        carta2.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().
            replace(R.id.Frame, SleepFragment()).
            addToBackStack(null).commit()
        }

        carta3.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().
            replace(R.id.Frame, PhysicalFragment()).
            addToBackStack(null).commit()
        }
        return home
    }

    fun actions(){

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}