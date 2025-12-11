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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import kotlin.math.roundToInt

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SleepFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adp: previousAdapter

    private val datesList = listOf(
        previousDataClass("Noviembre", "27", 50, R.color.sleep),
        previousDataClass("Noviembre", "28", 90, R.color.sleep),
        previousDataClass("Noviembre", "29", 70, R.color.sleep),
        previousDataClass("Noviembre", "30", 100, R.color.sleep),
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

        val sleep = inflater.inflate(R.layout.fragment_sleep, container, false)

        val scroller = sleep.findViewById<ScrollView>(R.id.sleepScroll)
        val progress = sleep.findViewById<ProgressBar>(R.id.progressBarSleep)

        val txtObjetivo = sleep.findViewById<TextView>(R.id.txtObjetivo)
        val txtProgreso = sleep.findViewById<TextView>(R.id.txtLitrostomados)
        val btnMenos = sleep.findViewById<TextView>(R.id.btnDisminuir)
        val btnMas = sleep.findViewById<TextView>(R.id.btnIncrementar)
        val btnEditar = sleep.findViewById<Button>(R.id.btnEditarObjetivo)
        val btnBack = sleep.findViewById<ImageView>(R.id.btnBackSleep)

        val anBar = barAnimation()

        // leer valores (INT) ya existentes
        val objetivo = sharedPreferencesApp.getInt("SleepGoal", 8)
        val progreso = sharedPreferencesApp.getInt("SleepProgress", 0)

        txtObjetivo.text = objetivo.toString()
        txtProgreso.text = progreso.toString()

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // animar barra al abrir
        val porcentaje = (progreso.toFloat() / objetivo.toFloat()) * 100
        anBar.animateProgress(progress, 0, porcentaje.toInt())

        // historial
        recyclerView = sleep.findViewById(R.id.historial)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adp = previousAdapter(datesList)
        recyclerView.adapter = adp
        recyclerView.scrollToPosition(adp.itemCount - 1)

        btnEditar.setOnClickListener {
            bottomSheetSleep().show(parentFragmentManager, "bottomSheetSleep")
        }

        // click normal +1
        btnMas.setOnClickListener {
            actualizarProgreso(
                txtProgreso, txtObjetivo, progress, anBar, sumar = true,
                btnMas, btnMenos
            )
        }

        // click normal -1
        btnMenos.setOnClickListener {
            actualizarProgreso(
                txtProgreso, txtObjetivo, progress, anBar, sumar = false,
                btnMas, btnMenos
            )
        }

        // long-click +2
        btnMas.setOnLongClickListener {
            val objetivoInt = txtObjetivo.text.toString().toInt()
            var progresoInt = txtProgreso.text.toString().toInt()

            val antes = ((progresoInt.toFloat() / objetivoInt) * 100).toInt()

            progresoInt += 2
            if (progresoInt > objetivoInt) progresoInt = objetivoInt

            txtProgreso.text = progresoInt.toString()
            sharedPreferencesApp.saveInt("SleepProgress", progresoInt)

            val despues = ((progresoInt.toFloat() / objetivoInt) * 100).toInt()
            anBar.animateProgress(progress, antes, despues)

            // actualizar botones
            validarBotones(txtProgreso, txtObjetivo, btnMas, btnMenos)

            // mostrar diálogo si se cumplió
            if (progresoInt >= objetivoInt) mostrarDialogo()

            // NOTIFICAR a Home para que actualice su barra también
            parentFragmentManager.setFragmentResult("home_sleep_refresh", Bundle())

            true
        }

        // opcional: long-click en menos (resta 2) — si querés lo agrego, por ahora no lo toco
        // scroller anim
        scroller.translationY = -100f
        scroller.alpha = 0f
        scroller.animate().translationY(0f).alpha(1f).setDuration(500).start()

        // cuando bottomsheet edite objetivo u otros refrescos manuales
        parentFragmentManager.setFragmentResultListener("sleep_refresh", this) { _, _ ->
            // refrescar pantalla del fragment
            refrescarPantalla(txtObjetivo, txtProgreso, progress)
            // también avisamos a home por si hace falta
            parentFragmentManager.setFragmentResult("home_sleep_refresh", Bundle())
        }

        validarBotones(txtProgreso, txtObjetivo, btnMas, btnMenos)

        return sleep
    }

    private fun actualizarProgreso(
        txtProgreso: TextView,
        txtObjetivo: TextView,
        progressBar: ProgressBar,
        anBar: barAnimation,
        sumar: Boolean,
        btnMas: TextView,
        btnMenos: TextView
    ) {
        val objetivo = txtObjetivo.text.toString().toInt()
        var progreso = txtProgreso.text.toString().toInt()

        val antes = ((progreso.toFloat() / objetivo) * 100).toInt()

        progreso = if (sumar) progreso + 1 else progreso - 1
        if (progreso < 0) progreso = 0

        txtProgreso.text = progreso.toString()

        // GUARDAR como INT (no toca DB)
        sharedPreferencesApp.saveInt("SleepProgress", progreso)

        val despues = ((progreso.toFloat() / objetivo) * 100).toInt()
        anBar.animateProgress(progressBar, antes, despues)

        validarBotones(txtProgreso, txtObjetivo, btnMas, btnMenos)

        if (progreso >= objetivo) mostrarDialogo()

        // NOTIFICAR a Home para que actualice la barra al volver
        parentFragmentManager.setFragmentResult("home_sleep_refresh", Bundle())
    }

    private fun refrescarPantalla(
        txtObjetivo: TextView,
        txtProgreso: TextView,
        progressBar: ProgressBar
    ) {
        val objetivo = sharedPreferencesApp.getInt("SleepGoal", 8)
        val progreso = sharedPreferencesApp.getInt("SleepProgress", 0)

        txtObjetivo.text = objetivo.toString()
        txtProgreso.text = progreso.toString()

        val porcentaje = ((progreso.toFloat() / objetivo) * 100).toInt()
        val anBar = barAnimation()
        anBar.animateProgress(progressBar, 0, porcentaje)
    }

    private fun validarBotones(
        txtProgreso: TextView,
        txtObjetivo: TextView,
        btnMas: TextView,
        btnMenos: TextView
    ) {
        val progreso = txtProgreso.text.toString().toInt()
        val objetivo = txtObjetivo.text.toString().toInt()

        if (progreso >= objetivo) {
            btnMas.isEnabled = false
            btnMenos.isEnabled = false
        } else {
            btnMas.isEnabled = true
            btnMenos.isEnabled = true
        }
    }

    private fun mostrarDialogo() {
        val dialogView = layoutInflater.inflate(R.layout.alertpopup, null)
        val btnOk = dialogView.findViewById<Button>(R.id.btnEntendido)
        val title = dialogView.findViewById<TextView>(R.id.txtHeaderText)
        val info = dialogView.findViewById<TextView>(R.id.txtAviso)
        val icon = dialogView.findViewById<LottieAnimationView>(R.id.AlertIcon)

        icon.setAnimation(R.raw.success)
        icon.loop(false)
        title.text = "¡Felicidades!"
        info.text = "Has cumplido tu objetivo de sueño"

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.show()
        btnOk.setOnClickListener { dialog.dismiss() }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SleepFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
