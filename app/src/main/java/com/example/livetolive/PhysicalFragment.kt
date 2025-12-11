package com.example.livetolive

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.livetolive.sharedPreferencesApp
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sqrt

class PhysicalFragment : Fragment(), SensorEventListener, ObjetivoCallback {

    private lateinit var sensorManager: SensorManager
    private lateinit var db: AppDatabase
    val colorConstante = R.color.pshy
    val listaPrevious = mutableListOf<previousDataClass>()
    private var stepSensor: Sensor? = null
    private var accelerometer: Sensor? = null
    private var ultimoPasoTime = 0L
    private var magnitudPrevia = 0f
    private var pasosIniciales = 0
    private var pasosPrev = 0
    private var distanciaPrev = 0.0
    private var caloriasPrev: Float = 0F
    private lateinit var txtPasos: TextView
    private lateinit var txtDistancia: TextView
    private lateinit var txtKcal: TextView
    private lateinit var txtRacha: TextView
    private lateinit var circular: CircularProgressBar
    private lateinit var circularDistancia: CircularProgressBar
    private lateinit var circularCalorias: CircularProgressBar
    private lateinit var txtPorcentajeProgreso: TextView
    private lateinit var txtDistanciaObj: TextView
    private lateinit var txtPasosObj: TextView
    private lateinit var txtKcalObj: TextView
    private lateinit var barraProgreso: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var barChart: BarChart
    private lateinit var adp: previousAdapter
    private lateinit var rachaIcon: LottieAnimationView
    private var dX = 0f
    private var dY = 0f
    private var lastAction = 0
    private var pasosHoy: Int = sharedPreferencesApp.getInt("ActividadProgress")
    private var objetivoPasos = 0
    private var objetivoDistancia = 3.0
    private var objetivoCalorias = 200
    private val PREFS = "fitness_data"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1001

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val phy = inflater.inflate(R.layout.fragment_physical, container, false)

        val scroller = phy.findViewById<ScrollView>(R.id.physicalScroll)
        barraProgreso = phy.findViewById(R.id.progressBarPhy)
        circular = phy.findViewById(R.id.circularProgressBar)
        circularDistancia = phy.findViewById(R.id.circularProgressBarDistancia)
        circularCalorias = phy.findViewById(R.id.circularProgressBarCalorias)
        txtPorcentajeProgreso = phy.findViewById(R.id.txtPorcentajeProgreso)
        rachaIcon = phy.findViewById(R.id.imgStreak)
        txtPasos = phy.findViewById(R.id.txtPasos)
        txtDistancia = phy.findViewById(R.id.txtDistancia)
        txtKcal = phy.findViewById(R.id.txtKcal)
        txtRacha = phy.findViewById(R.id.txtStreakCount)
        txtDistanciaObj = phy.findViewById(R.id.txtDistanciaObj)
        txtPasosObj = phy.findViewById(R.id.txtPasosObj)
        txtKcalObj = phy.findViewById(R.id.txtKcalObj)
        val imgBtnVolver = phy.findViewById<ImageView>(R.id.BtnBackHomeAF)
        val draggableCardView: CardView = phy.findViewById(R.id.cardViewDraggableStreak)

        imgBtnVolver.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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

        cargarObjetivos()

        phy.findViewById<RelativeLayout>(R.id.LayProgress).setOnClickListener { mostrarDialogObjetivo("pasos") }

        recyclerView = phy.findViewById(R.id.historial)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        cargarHistorial()
        recyclerView.scrollToPosition(adp.itemCount - 1)

        barChart = phy.findViewById(R.id.barChartWeek)
        configurarBarChart()

        // Inicializar la base de datos antes de llamar a cargarGraficaSemanal
        db = AppDatabase.getDatabase(requireContext())
        cargarGraficaSemanal()

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) ?: sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (stepSensor == null && accelerometer == null) {
            Log.e("PhysicalFragment", "No hay sensores disponibles")
            AlertDialog.Builder(requireContext())
                .setTitle("Sensores no disponibles")
                .setMessage("Tu dispositivo no tiene sensores para contar pasos.")
                .setPositiveButton("OK", null)
                .show()
        } else {
            Log.d("PhysicalFragment", "Sensores encontrados: stepSensor=${stepSensor != null}, accelerometer=${accelerometer != null}")
        }

        verificarPermisosSensor()
        cargarDatosHoy()

        txtRacha.text = obtenerRacha().toString()
        rachaIcon = phy.findViewById(R.id.imgStreak)

        val isRachaActiva = calcularProgresoTotal() >= 100
        actualizarEstadoRacha(isRachaActiva)

        draggableCardView.setOnTouchListener { v, event ->
            val parent = v.parent as View

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                    lastAction = MotionEvent.ACTION_DOWN
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val newY = event.rawY + dY

                    val parentWidth = parent.width - v.width
                    val parentHeight = parent.height - v.height

                    val boundedX = Math.max(0f, Math.min(newX, parentWidth.toFloat()))
                    val boundedY = Math.max(0f, Math.min(newY, parentHeight.toFloat()))

                    v.animate()
                        .x(boundedX)
                        .y(boundedY)
                        .setDuration(0)
                        .start()

                    lastAction = MotionEvent.ACTION_MOVE
                }

                MotionEvent.ACTION_UP -> {
                    if (lastAction == MotionEvent.ACTION_DOWN) {
                    }
                }
                else -> return@setOnTouchListener false
            }
            return@setOnTouchListener true
        }

        return phy
    }

    private fun verificarPermisosSensor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("PhysicalFragment", "Pidiendo permiso")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    PERMISSION_REQUEST_ACTIVITY_RECOGNITION
                )
            } else {
                registrarSensores()
            }
        } else {
            registrarSensores()
        }
    }

    private fun actualizarEstadoRacha(rachaActiva: Boolean) {
        if (rachaActiva) {
            rachaIcon.setAnimation(R.raw.racha_activada)
            rachaIcon.loop(true)
            rachaIcon.playAnimation()
        } else {
            rachaIcon.setAnimation(R.raw.racha_apagada)
            rachaIcon.loop(false)
            rachaIcon.pauseAnimation()
            rachaIcon.progress = 0f
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PhysicalFragment", "Permiso concedido")
                registrarSensores()
            } else {
                Log.e("PhysicalFragment", "Permiso denegado")
                AlertDialog.Builder(requireContext())
                    .setTitle("Permiso requerido")
                    .setMessage("Necesitas conceder el permiso para contar pasos.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }

    private fun registrarSensores() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("PhysicalFragment", "Step sensor registrado")
        }
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("PhysicalFragment", "Acelerómetro registrado")
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
            registrarSensores()
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        guardarDatosHoy()
        guardarObjetivos()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                val pasosTotalesSensor = event.values[0].toInt()

                verificarResetPorDia(pasosTotalesSensor)

                if (pasosIniciales == 0) {
                    val guardado = sharedPreferencesApp.getInt("pasosOffset", -1)
                    if (guardado != -1) {
                        pasosIniciales = guardado
                    } else {
                        pasosIniciales = pasosTotalesSensor
                        sharedPreferencesApp.saveInt("pasosOffset", pasosIniciales)
                    }
                }

                if (pasosTotalesSensor < pasosIniciales) {
                    pasosIniciales = 0
                    sharedPreferencesApp.saveInt("pasosOffset", 0)
                }

                pasosHoy = pasosTotalesSensor - pasosIniciales

                if (pasosHoy < 0) pasosHoy = 0

                Log.d("PhysicalFragment", "Pasos: $pasosHoy (Sensor: $pasosTotalesSensor - Offset: $pasosIniciales)")

                actualizarUI()
                verificarMetasIndividuales()
                verificarMetaTotal()
            }

            Sensor.TYPE_STEP_DETECTOR -> {
                val currentTime = System.currentTimeMillis()
                if (currentTime - ultimoPasoTime > 500) {
                    pasosHoy++
                    ultimoPasoTime = currentTime
                    actualizarUI()
                    verificarMetasIndividuales()
                    verificarMetaTotal()
                }
            }
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val magnitud = sqrt(x * x + y * y + z * z)
                val delta = magnitud - magnitudPrevia
                magnitudPrevia = magnitud

                if (delta > 6) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - ultimoPasoTime > 500) {
                        pasosHoy++
                        ultimoPasoTime = currentTime
                        actualizarUI()
                        verificarMetasIndividuales()
                        verificarMetaTotal()
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun cargarObjetivos() {
        val prefs = getPrefs()
        objetivoPasos = prefs.getInt("objetivoPasos", 3000)
        objetivoDistancia = prefs.getFloat("objetivoDistancia", 3.0f).toDouble()
        objetivoCalorias = prefs.getInt("objetivoCalorias", 200)
        objetivoPasos = sharedPreferencesApp.getInt("ActividadGoal", 0)
    }

    private fun guardarObjetivos() {
        getPrefs().edit()
            .putInt("objetivoPasos", objetivoPasos)
            .putFloat("objetivoDistancia", objetivoDistancia.toFloat())
            .putInt("objetivoCalorias", objetivoCalorias)
            .apply()
    }

    private fun mostrarDialogObjetivo(tipo: String) {
        if (tipo == "pasos") {
            val bottomSheet = PhysicalGoalText(this)
            bottomSheet.show(parentFragmentManager, "PhysicalGoalText")
        }
    }

    private fun cargarHistorial() {
        db = AppDatabase.getDatabase(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            db.actividadDao().getAll().collect { registros ->
                val listaTemp = mutableListOf<previousDataClass>()

                registros.forEach { h ->
                    val (mes, dia) = formatFecha(h.fecha)
                    val progreso = ((h.pasosRegistrados / h.pasosObjetivo) * 100).toInt()
                    listaTemp.add(previousDataClass(mes, dia, progreso, colorConstante))
                }

                CoroutineScope(Dispatchers.Main).launch {
                    listaPrevious.clear()
                    listaPrevious.addAll(listaTemp)
                    adp.notifyDataSetChanged()
                    recyclerView.scrollToPosition(adp.itemCount - 1)
                }
            }
        }

        adp = previousAdapter(listaPrevious)
        recyclerView.adapter = adp
    }

    fun formatFecha(date: Date): Pair<String, String> {
        val mes = SimpleDateFormat("MMMM", Locale("es", "ES")).format(date)
        val dia = SimpleDateFormat("d", Locale("es", "ES")).format(date)
        return Pair(mes, dia)
    }

    private fun cargarDatosDeDia(fecha: String) {
        pasosHoy = sharedPreferencesApp.getInt("ActividadProgress", 0)
        actualizarUI()
    }

    private fun configurarBarChart() {
        barChart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setPinchZoom(false)
            setScaleEnabled(false)
            setDrawValueAboveBar(true)
            axisRight.isEnabled = false
            axisLeft.apply {
                axisMinimum = 0f
                textColor = Color.BLACK
                textSize = 12f
                gridColor = Color.parseColor("#DDDDDD")
            }
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = Color.BLACK
                textSize = 12f
                granularity = 1f
            }
            legend.isEnabled = false
        }
    }

    // --- NUEVA FUNCIÓN CARGAR GRÁFICA SEMANAL CON DATOS REALES ---
    private fun cargarGraficaSemanal() {
        CoroutineScope(Dispatchers.IO).launch {
            // Recolectamos el flujo (Flow) de la BD
            db.actividadDao().getAll().collect { registros ->

                val entries = ArrayList<BarEntry>()
                val labels = ArrayList<String>()
                val hoy = Date()

                // Recorremos los últimos 7 días
                for (i in 6 downTo 0) {
                    val calendar = Calendar.getInstance()
                    calendar.time = hoy
                    calendar.add(Calendar.DAY_OF_YEAR, -i)
                    val fechaString = dateFormat.format(calendar.time)

                    var pasosDelDia = 0f

                    if (i == 0) {
                        // Si es HOY, tomamos el dato de la variable en vivo
                        pasosDelDia = pasosHoy.toFloat()
                    } else {
                        // Si es AYER o antes, buscamos en la BD
                        val registroEncontrado = registros.find {
                            dateFormat.format(it.fecha) == fechaString
                        }
                        if (registroEncontrado != null) {
                            pasosDelDia = registroEncontrado.pasosRegistrados.toFloat()
                        }
                    }

                    // Eje X: 0, 1, 2...
                    val xIndex = (6 - i).toFloat()
                    entries.add(BarEntry(xIndex, pasosDelDia))

                    // Etiquetas: L, M, M...
                    val nombreDia = SimpleDateFormat("E", Locale("es", "ES"))
                        .format(calendar.time).uppercase().substring(0, 1)
                    labels.add(nombreDia)
                }

                // Actualizamos la vista en el hilo principal
                CoroutineScope(Dispatchers.Main).launch {
                    val dataSet = BarDataSet(entries, "Pasos").apply {
                        color = Color.parseColor("#ad0000")
                        valueTextColor = Color.BLACK
                        valueTextSize = 10f
                        setDrawValues(false)
                    }

                    val data = BarData(dataSet)
                    data.barWidth = 0.5f

                    barChart.data = data
                    barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                    barChart.notifyDataSetChanged()
                    barChart.invalidate()
                    barChart.animateY(1000)
                }
            }
        }
    }
    // -------------------------------------------------------------

    private fun actualizarUI() {
        txtPasos.text = pasosHoy.toString()
        txtDistancia.text = String.format("%.2f", calcularDistancia())
        txtKcal.text = String.format("%.2f", calcularCalorias())
        txtDistanciaObj.text = "/${String.format("%.1f", objetivoDistancia)} km"
        txtPasosObj.text = "/$objetivoPasos pasos"
        txtKcalObj.text = "/$objetivoCalorias kcal"
        sharedPreferencesApp.saveInt("ActividadProgress", pasosHoy)
        sharedPreferencesApp.saveInt("ActividadGoal", objetivoPasos)

        circular.progressMax = objetivoPasos.toFloat()
        circular.setProgressWithAnimation(pasosHoy.toFloat(), circular.progressMax.toLong(), AccelerateDecelerateInterpolator())
        circularDistancia.progressMax = (objetivoDistancia * 1000).toFloat()
        circularDistancia.setProgressWithAnimation((calcularDistancia() * 1000).toFloat(), circularDistancia.progressMax.toLong(), AccelerateDecelerateInterpolator())
        circularCalorias.progressMax = objetivoCalorias.toFloat()
        circularCalorias.setProgressWithAnimation(calcularCalorias().toFloat(), circularCalorias.progressMax.toLong(), AccelerateDecelerateInterpolator())

        txtPorcentajeProgreso.text = "${calcularProgresoTotal().toInt()}%"
        barraProgreso.progress = calcularProgresoTotal().toInt()

        txtRacha.text = obtenerRacha().toString()
        val isRachaActiva = calcularProgresoTotal() >= 100
        actualizarEstadoRacha(isRachaActiva)

        // OPCIONAL: Si quieres que la gráfica se actualice en tiempo real con cada paso
        // puedes llamar a cargarGraficaSemanal() aquí, pero consume más recursos.
        // Con hacerlo en onCreateView suele ser suficiente.
    }

    private fun calcularDistancia(): Double = (pasosHoy * 0.6) / 1000
    private fun calcularCalorias(): Double = pasosHoy * 0.04

    private fun calcularProgresoTotal(): Double {
        val p1 = pasosHoy / objetivoPasos.toFloat()
        val p2 = calcularDistancia() / objetivoDistancia
        val p3 = calcularCalorias() / objetivoCalorias.toFloat()
        return ((p1 + p2 + p3) / 3f * 100f).coerceAtMost(100.0)
    }

    private fun verificarMetasIndividuales() {

        if (pasosPrev < objetivoPasos && pasosHoy >= objetivoPasos) {
            mostrarPopupPersonalizado("¡Objetivo de pasos cumplido!", R.raw.success)
        }
        pasosPrev = pasosHoy
    }

    private fun mostrarPopupPersonalizado(mensaje: String, animacion: Int) {
        val dialogView = layoutInflater.inflate(R.layout.alertpopup, null)

        val btnDialog = dialogView.findViewById<Button>(R.id.btnEntendido)
        val infoPopup = dialogView.findViewById<TextView>(R.id.txtAviso)
        val titlePopup = dialogView.findViewById<TextView>(R.id.txtHeaderText)
        val iconPopup = dialogView.findViewById<LottieAnimationView>(R.id.AlertIcon)

        iconPopup.setAnimation(animacion)
        iconPopup.loop(false)
        iconPopup.playAnimation()

        titlePopup.text = "¡FELICIDADES!"
        infoPopup.text = mensaje

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.show()

        btnDialog.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun verificarMetaTotal() {
        if (calcularProgresoTotal() >= 100 && !yaSumoRachaHoy()) {
            sumarRacha()
        }
    }

    private fun sumarRacha() {
        val nueva = obtenerRacha() + 1
        sharedPreferencesApp.saveInt("racha", nueva)
        sharedPreferencesApp.saveString("ultimaFechaStreak", fechaHoy())
        sharedPreferencesApp.saveBoolean("sumadoHoy", true)
        txtRacha.text = nueva.toString()
        actualizarEstadoRacha(true)
    }

    private fun yaSumoRachaHoy(): Boolean {
        return sharedPreferencesApp.getBoolean("sumadoHoy", false)
    }

    private fun obtenerRacha(): Int = sharedPreferencesApp.getInt("racha", 0)

    private fun verificarResetPorDia(rawPasosSensor: Int) {
        val prefs = getPrefs()
        val ultima = prefs.getString("ultimaFecha", "") ?: ""
        val hoy = fechaHoy()

        if (ultima != hoy) {

            if (ultima.isNotEmpty()) {
                val progresoAyer = prefs.getInt("progreso_$ultima", 0)
                val rachaPerdida = obtenerRacha()
                if (progresoAyer < 100 && rachaPerdida > 0) {
                    mostrarPopupRachaPerdida(rachaPerdida)
                    sharedPreferencesApp.saveInt("racha", 0)
                }
            }

            pasosIniciales = rawPasosSensor
            sharedPreferencesApp.saveInt("pasosOffset", pasosIniciales)

            pasosHoy = 0
            sharedPreferencesApp.saveBoolean("sumadoHoy", false)
            sharedPreferencesApp.saveInt("ActividadProgress", 0)

            prefs.edit()
                .putInt("pasosHoy", 0)
                .putString("ultimaFecha", hoy)
                .apply()

            cargarHistorial()
            actualizarUI()
            // Recargamos la gráfica porque cambió el día
            cargarGraficaSemanal()
        }
    }

    private fun mostrarPopupRachaPerdida(racha: Int) {
        mostrarDialgo(racha)
    }

    private fun cargarDatosHoy() {
        val prefs = getPrefs()
        pasosHoy = sharedPreferencesApp.getInt("ActividadProgress", 0)
        pasosIniciales = sharedPreferencesApp.getInt("pasosOffset", 0)

        pasosPrev = pasosHoy
        distanciaPrev = prefs.getFloat("distancia_${fechaHoy()}", calcularDistancia().toFloat()).toDouble()

        try {
            caloriasPrev = prefs.getFloat("calorias_${fechaHoy()}", calcularCalorias().toFloat())
        } catch (e: ClassCastException) {
            val caloriasViejas = prefs.getInt("calorias_${fechaHoy()}", calcularCalorias().toInt())
            caloriasPrev = caloriasViejas.toFloat()
            prefs.edit().putFloat("calorias_${fechaHoy()}", caloriasPrev).apply()
        }
        actualizarUI()
    }

    private fun guardarDatosHoy() {
        val prefs = getPrefs()
        val fecha = fechaHoy()
        prefs.edit()
            .putInt("pasosHoy", pasosHoy)
            .putInt("pasos_$fecha", pasosHoy)
            .putFloat("distancia_$fecha", calcularDistancia().toFloat())
            .putFloat("calorias_$fecha", calcularCalorias().toFloat())
            .putInt("progreso_$fecha", calcularProgresoTotal().toInt())
            .putString("ultimaFecha", fecha)
            .apply()
    }

    private fun fechaHoy(): String = dateFormat.format(Date())

    private fun getPrefs() = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)


    override fun onGoalSet(nuevoObjetivoPasos: Int) {
        objetivoPasos = nuevoObjetivoPasos
        objetivoDistancia = (objetivoPasos * 0.6) / 1000.0
        objetivoCalorias = (objetivoPasos * 0.04).toInt()

        guardarObjetivos()
        actualizarUI()
    }

    fun mostrarDialgo(rachaPerdida: Int){
        val dialogView=layoutInflater.inflate(R.layout.alertpopup,null)
        val btnDialog=dialogView.findViewById<Button>(R.id.btnEntendido)
        val infoPopup=dialogView.findViewById<TextView>(R.id.txtAviso)
        val titlePopup=dialogView.findViewById<TextView>(R.id.txtHeaderText)
        val iconPopup=dialogView.findViewById<LottieAnimationView>(R.id.AlertIcon)
        iconPopup.setAnimation(R.raw.racha_perdida)
        iconPopup.speed=0.5f
        iconPopup.loop(true)
        titlePopup.text="¡RACHA PERDIDA!"
        val mensaje = "¡Oh no! Has perdido tu racha de ${rachaPerdida} días por no cumplir tus objetivos ayer. ¡No te rindas, vuelve a empezar hoy!"
        infoPopup.text = mensaje
        val dialog= androidx.appcompat.app.AlertDialog.Builder(requireContext()).setView(dialogView).create()
        dialog.show()
        btnDialog.setOnClickListener {
            dialog.dismiss()
        }
    }
}