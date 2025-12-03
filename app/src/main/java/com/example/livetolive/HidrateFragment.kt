package com.example.livetolive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import kotlin.math.round
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HidrateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HidrateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adp: previousAdapter
    private val datesList=listOf(
        previousDataClass("Noviembre","27",50, R.color.hidra),
        previousDataClass("Noviembre","28",90,R.color.hidra),
        previousDataClass("Noviembre","29",70,R.color.hidra),
        previousDataClass("Noviembre","30",100,R.color.hidra),
    )

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
        val hidrate= inflater.inflate(R.layout.fragment_hidrate, container, false)
        val scroller=hidrate.findViewById<ScrollView>(R.id.hidrateScroll)
        val hidrateProgress=hidrate.findViewById<ProgressBar>(R.id.progressBarHidra)
        val btnDisminuir=hidrate.findViewById<TextView>(R.id.btnDisminuir)
        val btnAumentar=hidrate.findViewById<TextView>(R.id.btnIncrementar)
        val LitrosTomados=hidrate.findViewById<TextView>(R.id.txtLitrostomados)
        val objetivo=hidrate.findViewById<TextView>(R.id.txtObjetivo)
        LitrosTomados.text= sharedPreferencesApp.getFloat("HidratationProgress").toString()
        val anBar= barAnimation()
        recyclerView=hidrate.findViewById(R.id.historial)
        recyclerView.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        adp= previousAdapter(datesList)
        recyclerView.adapter=adp
        recyclerView.scrollToPosition(adp.itemCount - 1)

        var porcentajeProgreso=(LitrosTomados.text.toString().toFloat()/objetivo.text.toString().toFloat())*100
        anBar.animateProgress(hidrateProgress,0,porcentajeProgreso.toInt())

        btnDisminuir.setOnClickListener {
            var porcentajeAnterior=(LitrosTomados.text.toString().toFloat()/objetivo.text.toString().toFloat())*100
            var Progreso: Float=LitrosTomados.text.toString().toFloat()
            if(Progreso>0){
                Progreso= ((Progreso - 0.1f) * 10).roundToInt() / 10f
            }
            porcentajeProgreso=(LitrosTomados.text.toString().toFloat()/objetivo.text.toString().toFloat())*100
            LitrosTomados.text=Progreso.toString()
            anBar.animateProgress(hidrateProgress,porcentajeAnterior.toInt(),porcentajeProgreso.toInt())
            saveProgress(Progreso)
        }

        btnDisminuir.setOnLongClickListener {
            var porcentajeAnterior=(LitrosTomados.text.toString().toFloat()/objetivo.text.toString().toFloat())*100
            var Progreso: Float=LitrosTomados.text.toString().toFloat()
            if(Progreso>=1){
                Progreso= ((Progreso - 1f) * 10).roundToInt() / 10f
            }
            LitrosTomados.text=Progreso.toString()
            porcentajeProgreso=(LitrosTomados.text.toString().toFloat()/objetivo.text.toString().toFloat())*100
            anBar.animateProgress(hidrateProgress,porcentajeAnterior.toInt(),porcentajeProgreso.toInt())
            saveProgress(Progreso)
            true
        }

        btnAumentar.setOnClickListener {
            var porcentajeAnterior=(LitrosTomados.text.toString().toFloat()/objetivo.text.toString().toFloat())*100
            var Progreso: Float=LitrosTomados.text.toString().toFloat()
            if(Progreso>=0){
                Progreso= ((Progreso + 0.1f) * 10).roundToInt() / 10f
            }
            LitrosTomados.text=Progreso.toString()
            porcentajeProgreso=(LitrosTomados.text.toString().toFloat()/objetivo.text.toString().toFloat())*100
            anBar.animateProgress(hidrateProgress,porcentajeAnterior.toInt(),porcentajeProgreso.toInt())
            saveProgress(Progreso)
        }

        btnAumentar.setOnLongClickListener {
            var porcentajeAnterior=(LitrosTomados.text.toString().toFloat()/objetivo.text.toString().toFloat())*100
            var Progreso: Float=LitrosTomados.text.toString().toFloat()
            if(Progreso>=0){
                Progreso= ((Progreso + 1f) * 10).roundToInt() / 10f
            }
            LitrosTomados.text=Progreso.toString()
            porcentajeProgreso=(LitrosTomados.text.toString().toFloat()/objetivo.text.toString().toFloat())*100
            anBar.animateProgress(hidrateProgress,porcentajeAnterior.toInt(),porcentajeProgreso.toInt())
            saveProgress(Progreso)
            true
        }

        scroller.apply {
            translationY = -100f
            alpha = 0f

            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500)
                .start()
        }
        return hidrate
    }

    fun saveProgress(progreso: Float){
        sharedPreferencesApp.saveFloat("HidratationProgress",progreso)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HidrateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HidrateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}