package com.example.livetolive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.livetolive.databinding.FragmentHidrateBinding
import com.airbnb.lottie.LottieAnimationView
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
    lateinit var binding: FragmentHidrateBinding
    private lateinit var taskViewModel: HidrateViewModel
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
    ): View {

        binding = FragmentHidrateBinding.inflate(inflater, container, false)


        taskViewModel = ViewModelProvider(this).get(HidrateViewModel::class.java)


        val scroller = binding.hidrateScroll
        val hidrateProgress = binding.progressBarHidra
        val btnDisminuir = binding.btnDisminuir
        val btnAumentar = binding.btnIncrementar
        val LitrosTomados = binding.txtLitrostomados
        val objetivo = binding.txtObjetivo
        val imgBtnVolver = binding.btnBack


        objetivo.text=sharedPreferencesApp.getFloat("HidrateGoal").toString()

        binding.btnEditarObjetivo.setOnClickListener {
            val bottom = bottomSheetHidrate()
            bottom.show(parentFragmentManager, "bottomSheet")
        }

        imgBtnVolver.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        LitrosTomados.text = sharedPreferencesApp.getFloat("HidratationProgress").toString()

        val anBar = barAnimation()


        recyclerView = binding.historial
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        adp = previousAdapter(datesList)
        recyclerView.adapter = adp
        recyclerView.scrollToPosition(adp.itemCount - 1)

        var porcentajeProgreso =
            (LitrosTomados.text.toString().toFloat() / objetivo.text.toString().toFloat()) * 100
        anBar.animateProgress(hidrateProgress, 0, porcentajeProgreso.toInt())



        btnDisminuir.setOnClickListener {
            var porcentajeAnterior =
                (LitrosTomados.text.toString().toFloat() / objetivo.text.toString().toFloat()) * 100

            var Progreso = LitrosTomados.text.toString().toFloat()
            if (Progreso > 0) {
                Progreso = ((Progreso - 0.1f) * 10).roundToInt() / 10f
            }

            LitrosTomados.text = Progreso.toString()

            porcentajeProgreso =
                (LitrosTomados.text.toString().toFloat() / objetivo.text.toString().toFloat()) * 100

            anBar.animateProgress(hidrateProgress, porcentajeAnterior.toInt(), porcentajeProgreso.toInt())
            saveProgress(Progreso)
        }

        btnDisminuir.setOnLongClickListener {
            val porcentajeAnterior = (LitrosTomados.text.toString().toFloat() / objetivo.text.toString().toFloat()) * 100
            var Progreso: Float = LitrosTomados.text.toString().toFloat()
            if (Progreso >= 1) {
                Progreso = ((Progreso - 1f) * 10).roundToInt() / 10f
            }
            LitrosTomados.text = Progreso.toString()
            val porcentajeProgreso = (Progreso / objetivo.text.toString().toFloat()) * 100
            anBar.animateProgress(hidrateProgress, porcentajeAnterior.toInt(), porcentajeProgreso.toInt())
            saveProgress(Progreso)
            true
        }


        btnAumentar.setOnClickListener {
            var porcentajeAnterior =
                (LitrosTomados.text.toString().toFloat() / objetivo.text.toString().toFloat()) * 100

            var Progreso = LitrosTomados.text.toString().toFloat()
            Progreso = ((Progreso + 0.1f) * 10).roundToInt() / 10f

            LitrosTomados.text = Progreso.toString()
            if (Progreso >= objetivo.text.toString().toFloat()) {
                mostrarDialgo()
            }

            porcentajeProgreso =
                (LitrosTomados.text.toString().toFloat() / objetivo.text.toString().toFloat()) * 100

            anBar.animateProgress(hidrateProgress, porcentajeAnterior.toInt(), porcentajeProgreso.toInt())
            objCumplido()
            saveProgress(Progreso)
        }

        btnAumentar.setOnLongClickListener {
            val porcentajeAnterior = (LitrosTomados.text.toString().toFloat() / objetivo.text.toString().toFloat()) * 100
            var Progreso: Float = LitrosTomados.text.toString().toFloat()
            if (Progreso >= 0) {
                Progreso = ((Progreso + 1f) * 10).roundToInt() / 10f
            }
            LitrosTomados.text = Progreso.toString()
            if (Progreso >= objetivo.text.toString().toFloat()) {
                mostrarDialgo()
            }
            LitrosTomados.text = Progreso.toString()
            val porcentajeProgreso = (Progreso / objetivo.text.toString().toFloat()) * 100
            anBar.animateProgress(hidrateProgress, porcentajeAnterior.toInt(), porcentajeProgreso.toInt())
            saveProgress(Progreso)
            objCumplido()
            true
        }




        // Animación
        scroller.apply {
            translationY = -100f
            alpha = 0f

            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500)
                .start()
        }

        objCumplido()

        return binding.root
    }

    fun mostrarDialgo(){
        val dialogView=layoutInflater.inflate(R.layout.alertpopup,null)
        val btnDialog=dialogView.findViewById<Button>(R.id.btnEntendido)
        val infoPopup=dialogView.findViewById<TextView>(R.id.txtAviso)
        val titlePopup=dialogView.findViewById<TextView>(R.id.txtHeaderText)
        val iconPopup=dialogView.findViewById<LottieAnimationView>(R.id.AlertIcon)
        iconPopup.setAnimation(R.raw.success)
        iconPopup.loop(false)
        titlePopup.text="¡FELICIDADES!"
        infoPopup.text="Haz cumplido con el objetivo de hidratación el dia de hoy"
        val dialog= AlertDialog.Builder(requireContext()).setView(dialogView).create()
        dialog.show()
        btnDialog.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun objCumplido(){
        if(binding.txtObjetivo.text.toString().toFloat()<=binding.txtLitrostomados.text.toString().toFloat()) {
            binding.btnDisminuir.isEnabled = false
            TextInactivo(binding.btnDisminuir)
            binding.btnIncrementar.isEnabled = false
            TextInactivo(binding.btnIncrementar)
        }else{
            binding.btnDisminuir.isEnabled = true
            binding.btnIncrementar.isEnabled = true
        }
    }

    fun TextInactivo(text:TextView){
        text.setTextColor(resources.getColor(R.color.hidra))
        text.backgroundTintList=resources.getColorStateList(R.color.gray)
    }

    fun btnInactivo(btn:Button){
        btn.setTextColor(resources.getColor(R.color.hidra))
        btn.backgroundTintList=resources.getColorStateList(R.color.gray)
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