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
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var miDb: AppDatabase
    private fun isNewDay(lastTimestamp: Long): Boolean {
        val calendarLast = Calendar.getInstance().apply { timeInMillis = lastTimestamp }
        val calendarToday = Calendar.getInstance()
        return calendarToday.get(Calendar.YEAR) != calendarLast.get(Calendar.YEAR) ||
                calendarToday.get(Calendar.DAY_OF_YEAR) != calendarLast.get(Calendar.DAY_OF_YEAR)
    }
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val home =inflater.inflate(R.layout.fragment_home, container, false)
        miDb = AppDatabase.getDatabase(requireContext())
        var porcentajeActividad:Int=0
        val lastDay = sharedPreferencesApp.getLastDay()
        val today = Calendar.getInstance().timeInMillis
        //Cartas
        val carta1=home.findViewById<CardView>(R.id.card)
        val carta2=home.findViewById<CardView>(R.id.card2)
        val carta3=home.findViewById<CardView>(R.id.card3)

        //Barras De progresso
        val progre1=home.findViewById<ProgressBar>(R.id.progressBar)
        val progre2=home.findViewById<ProgressBar>(R.id.progressBar2)
        val progre3=home.findViewById<ProgressBar>(R.id.progressBar3)

        //Titulos de las Cartas
        val txtprogre1=home.findViewById<TextView>(R.id.txtProgreso)
        val txtprogre2=home.findViewById<TextView>(R.id.txtProgreso2)

        //Labels de los Objetivos
        val txtobj1=home.findViewById<TextView>(R.id.txtObjetivo)
        val txtobj2=home.findViewById<TextView>(R.id.txtObjetivo2)
        val txtobj3=home.findViewById<TextView>(R.id.txtPasos)


        //Elementos del calendario
        val txtd1=home.findViewById<TextView>(R.id.txtd1)
        val txtd2=home.findViewById<TextView>(R.id.txtd2)
        val txtd3=home.findViewById<TextView>(R.id.txtd3)
        val txtd4=home.findViewById<TextView>(R.id.txtd4)
        val txtd5=home.findViewById<TextView>(R.id.txtd5)
        val txtd6=home.findViewById<TextView>(R.id.txtd6)
        val txtd7=home.findViewById<TextView>(R.id.txtd7)
        val mesActual=home.findViewById<TextView>(R.id.mesActual)
        val scrollview=home.findViewById<ScrollView>(R.id.scroller)
        val diasSemana = obtenerDiasSemana()
        val hoy = numeroDiaHoy()
        var porcentajeAgua: Int=0

        //Cargar datos del calendario
        mesActual.text=mesActual().uppercase()
        txtd1.text=diasSemana[0].toString()
        if (hoy==diasSemana[0]){
            todayIdicator(txtd1)
        }
        txtd2.text=diasSemana[1].toString()
        if (hoy==diasSemana[1]){
            todayIdicator(txtd2)
        }
        txtd3.text=diasSemana[2].toString()
        if (hoy==diasSemana[2]){
            todayIdicator(txtd3)
        }
        txtd4.text=obtenerDiasSemana()[3].toString()
        if (hoy==diasSemana[3]){
            todayIdicator(txtd4)
        }
        txtd5.text=obtenerDiasSemana()[4].toString()
        if (hoy==diasSemana[4]){
            todayIdicator(txtd5)
        }
        txtd6.text=obtenerDiasSemana()[5].toString()
        if (hoy==diasSemana[5]){
            todayIdicator(txtd6)
        }
        txtd7.text=obtenerDiasSemana()[6].toString()
        if (hoy==diasSemana[6]){
            todayIdicator(txtd7)
        }

        //Carga los datos que estan en el sharedPreferences Dentro de las cardvius

        if (isNewDay(lastDay)) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = lastDay
            cal.set(Calendar.HOUR_OF_DAY, 12)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            lifecycleScope.launch {
                val registro = Hidratacion(
                    litrosObjetivo = sharedPreferencesApp.getFloat("HidrateGoal"),
                    litrosRegistrados = sharedPreferencesApp.getFloat("HidratationProgress"),
                    fecha = cal.time
                )
                miDb.hidratacionDao().insert(registro)
                sharedPreferencesApp.saveFloat("HidratationProgress",0f)
                txtobj1.text=sharedPreferencesApp.getFloat("HidrateGoal").toString()+" L"
                porcentajeAgua= ((sharedPreferencesApp.getFloat("HidratationProgress")/sharedPreferencesApp.getFloat("HidrateGoal"))*100).toInt()
                txtprogre1.text=sharedPreferencesApp.getFloat("HidratationProgress").toString()+" L"
            }

            lifecycleScope.launch {
                val registro= Sleep(
                    horasObjetivo = sharedPreferencesApp.getInt("SleepGoal").toFloat(),
                    horasRegistradas = sharedPreferencesApp.getInt("SleepProgress").toFloat(),
                    fecha = cal.time
                )
                miDb.sleepDao().insert(registro)
                sharedPreferencesApp.saveInt("SleepProgress",0)
                txtobj2.text=sharedPreferencesApp.getInt("SleepGoal").toString()+" H"
                txtprogre2.text=sharedPreferencesApp.getInt("SleepProgress").toString()+" H"
            }

            lifecycleScope.launch {
                val registro= Actividad(
                    pasosObjetivo = sharedPreferencesApp.getInt("ActividadGoal"),
                    pasosRegistrados = sharedPreferencesApp.getInt("ActividadProgress"),
                    fecha = cal.time
                )
                miDb.actividadDao().insert(registro)
                sharedPreferencesApp.saveInt("ActividadProgress",0)
                txtobj3.text=sharedPreferencesApp.getInt("ActividadProgress").toString()+"/"+sharedPreferencesApp.getInt("ActividadGoal").toString()
                porcentajeActividad= ((sharedPreferencesApp.getInt("ActividadProgress").toFloat() / sharedPreferencesApp.getInt("ActividadGoal").toFloat())*100).toInt()

            }
            sharedPreferencesApp.saveLastDay(today)


        }else{
            txtobj1.text=sharedPreferencesApp.getFloat("HidrateGoal").toString()+" L"
            porcentajeAgua= ((sharedPreferencesApp.getFloat("HidratationProgress")/sharedPreferencesApp.getFloat("HidrateGoal"))*100).toInt()
            txtprogre1.text=sharedPreferencesApp.getFloat("HidratationProgress").toString()+" L"
        }


        txtobj2.text=sharedPreferencesApp.getInt("SleepGoal").toString()+" H"
        // <<< AÑADIDO: usar Float en la división para evitar truncamiento entero que daba 0
        val porcentajeSueño= ((sharedPreferencesApp.getInt("SleepProgress").toFloat() / sharedPreferencesApp.getInt("SleepGoal").toFloat())*100).toInt()
        // <<< FIN AÑADIDO
        txtprogre2.text=sharedPreferencesApp.getInt("SleepProgress").toString()+" H"

        txtobj3.text=sharedPreferencesApp.getInt("ActividadProgress").toString()+"/"+sharedPreferencesApp.getInt("ActividadGoal").toString()
        // <<< AÑADIDO: usar Float en la división para evitar truncamiento entero que daba 0
        porcentajeActividad= ((sharedPreferencesApp.getInt("ActividadProgress").toFloat() / sharedPreferencesApp.getInt("ActividadGoal").toFloat())*100).toInt()
        // <<< FIN AÑADIDO


        val anBar= barAnimation()

        anBar.animateProgress(progre1,0,porcentajeAgua.toInt())
        anBar.animateProgress(progre2,0,porcentajeSueño.toInt())
        anBar.animateProgress(progre3,0,porcentajeActividad.toInt())

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

        // <<< AÑADIDO: escuchar notificación de SleepFragment para actualizar barra de sueño en Home
        parentFragmentManager.setFragmentResultListener("home_sleep_refresh", viewLifecycleOwner) { _, _ ->
            txtobj2.text = sharedPreferencesApp.getInt("SleepGoal").toString() + " H"
            txtprogre2.text = sharedPreferencesApp.getInt("SleepProgress").toString() + " H"

            val porcentaje = ((sharedPreferencesApp.getInt("SleepProgress").toFloat() / sharedPreferencesApp.getInt("SleepGoal").toFloat()) * 100).toInt()
            anBar.animateProgress(progre2, 0, porcentaje)
        }
        // <<< FIN AÑADIDO

        return home
    }

    fun todayIdicator(textView: TextView){
        textView.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.indicatorC)
        )
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    fun numeroDiaHoy(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.DAY_OF_MONTH)
    }
    fun obtenerDiasSemana(): List<Int>{
        val cal= Calendar.getInstance()
        val diaHoy=cal.get(Calendar.DAY_OF_WEEK)

        val retroceder= if (diaHoy == Calendar.SUNDAY) 6 else diaHoy - Calendar.MONDAY
        cal.add(Calendar.DAY_OF_MONTH,-retroceder)
        return (0..6).map {
            if (it != 0) cal.add(Calendar.DAY_OF_MONTH, 1)
            cal.get(Calendar.DAY_OF_MONTH)
        }
    }

    fun mesActual():String{
        val cal=Calendar.getInstance()
        val formatollo= SimpleDateFormat("MMMM", Locale("es"))
        return formatollo.format(cal.time)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of fragment HomeFragment.
         */
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
