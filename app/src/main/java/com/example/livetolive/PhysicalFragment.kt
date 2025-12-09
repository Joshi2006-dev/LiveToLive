package com.example.livetolive

import android.Manifest
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
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sqrt

class PhysicalFragment : Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null  // Sensor de pasos (del video)
    private var accelerometer: Sensor? = null  // Acelerómetro para detección alternativa (del video)
    private var ultimoPasoTime = 0L
    private var magnitudPrevia = 0f  // Para detectar picos en acelerómetro
    private var pasosIniciales = 0  // Para TYPE_STEP_COUNTER

    private var pasosPrev = 0
    private var distanciaPrev = 0.0
    private var caloriasPrev = 0

    private lateinit var txtPasos: TextView
    private lateinit var txtDistancia: TextView
    private lateinit var txtCalorias: TextView
    private lateinit var txtRacha: TextView
    private lateinit var txtPasosTotales: TextView
    private lateinit var txtPorcentaje: TextView
    private lateinit var txtKcalObjetivo: TextView
    private lateinit var txtDistanceGoal: TextView
    private lateinit var circular: CircularProgressBar
    private lateinit var barraProgreso: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var barChart: BarChart
    private lateinit var adp: previousAdapter
    private lateinit var rachaIcon: LottieAnimationView
    private var pasosHoy = 0
    private var objetivoPasos = 6000
    private var objetivoDistancia = 3.0
    private var objetivoCalorias = 200
    private val PREFS = "fitness_data"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val phy = inflater.inflate(R.layout.fragment_physical, container, false)

        // Tus inicializaciones existentes
        val scroller = phy.findViewById<ScrollView>(R.id.physicalScroll)
        barraProgreso = phy.findViewById(R.id.progressBarPhy)
        circular = phy.findViewById(R.id.circularProgressBar)
        rachaIcon = phy.findViewById(R.id.imgStreak)
        txtPasos = phy.findViewById(R.id.contadorpasos)
        txtDistancia = phy.findViewById(R.id.txtDistance)
        txtCalorias = phy.findViewById(R.id.txtCalBurn)
        txtRacha = phy.findViewById(R.id.txtStreakCount)
        txtPasosTotales = phy.findViewById(R.id.pasos_totales)
        txtPorcentaje = phy.findViewById(R.id.porcentaje)
        txtKcalObjetivo = phy.findViewById(R.id.txtKcalObjetivo)
        txtDistanceGoal = phy.findViewById(R.id.distanceGoal)

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
        (phy.findViewById<TextView>(R.id.txtLabelCalBurn).parent as? View)?.setOnClickListener { mostrarDialogObjetivo("calorias") }
        (phy.findViewById<TextView>(R.id.txtDistanceLabel).parent as? View)?.setOnClickListener { mostrarDialogObjetivo("distancia") }

        // Simulación para pruebas (del video)
        phy.findViewById<TextView>(R.id.contadorpasos).setOnLongClickListener {
            pasosHoy += 100
            actualizarUI()
            true
        }

        recyclerView = phy.findViewById(R.id.historial)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rellenarHistorialSiVacio()
        cargarHistorial()
        recyclerView.scrollToPosition(adp.itemCount - 1)

        barChart = phy.findViewById(R.id.barChartWeek)
        configurarBarChart()
        cargarGraficaSemanal()

        // Inicialización del sensor (del video)
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

        verificarResetPorDia()
        cargarDatosHoy()

        return phy
    }

    // Permisos (del video)
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
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                val pasosTotales = event.values[0].toInt()
                if (pasosIniciales == 0) pasosIniciales = pasosTotales
                pasosHoy = pasosTotales - pasosIniciales
                Log.d("PhysicalFragment", "Pasos counter: $pasosHoy")
                actualizarUI()
                verificarMetasIndividuales()
                verificarMetaTotal()
            }
            Sensor.TYPE_STEP_DETECTOR -> {
                val currentTime = System.currentTimeMillis()
                if (currentTime - ultimoPasoTime > 500) {
                    pasosHoy++
                    ultimoPasoTime = currentTime
                    Log.d("PhysicalFragment", "Paso detector: $pasosHoy")
                    actualizarUI()
                    verificarMetasIndividuales()
                    verificarMetaTotal()
                }
            }
            Sensor.TYPE_ACCELEROMETER -> {
                // Detección alternativa con acelerómetro (del video)
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val magnitud = sqrt(x * x + y * y + z * z)
                val delta = magnitud - magnitudPrevia
                magnitudPrevia = magnitud

                if (delta > 6) {  // Umbral para detectar "paso" (ajusta según pruebas)
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - ultimoPasoTime > 500) {
                        pasosHoy++
                        ultimoPasoTime = currentTime
                        Log.d("PhysicalFragment", "Paso acelerómetro: $pasosHoy")
                        actualizarUI()
                        verificarMetasIndividuales()
                        verificarMetaTotal()
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // Tus funciones existentes (sin cambios)
    private fun cargarObjetivos() {
        val prefs = getPrefs()
        objetivoPasos = prefs.getInt("objetivoPasos", 6000)
        objetivoDistancia = prefs.getFloat("objetivoDistancia", 3.0f).toDouble()
        objetivoCalorias = prefs.getInt("objetivoCalorias", 200)
    }

    private fun guardarObjetivos() {
        getPrefs().edit()
            .putInt("objetivoPasos", objetivoPasos)
            .putFloat("objetivoDistancia", objetivoDistancia.toFloat())
            .putInt("objetivoCalorias", objetivoCalorias)
            .apply()
    }

    private fun mostrarDialogObjetivo(tipo: String) {
        val builder = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())
        input.hint = when (tipo) {
            "pasos" -> "Nuevo objetivo de pasos (ej., 6000)"
            "calorias" -> "Nuevo objetivo de calorías (ej., 200)"
            "distancia" -> "Nuevo objetivo de distancia en km (ej., 3.0)"
            else -> ""
        }
        builder.setView(input)
        builder.setPositiveButton("Guardar") { _, _ ->
            val nuevoValor = input.text.toString().toDoubleOrNull()
            if (nuevoValor != null && nuevoValor > 0) {
                when (tipo) {
                    "pasos" -> objetivoPasos = nuevoValor.toInt()
                    "calorias" -> objetivoCalorias = nuevoValor.toInt()
                    "distancia" -> objetivoDistancia = nuevoValor
                }
                guardarObjetivos()
                actualizarUI()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun rellenarHistorialSiVacio() {
        val prefs = getPrefs()
        val hoy = Date()
        val random = Random()
        for (i in 6 downTo 0) {
            val calendar = Calendar.getInstance().apply { time = hoy; add(Calendar.DAY_OF_MONTH, -i) }
            val fecha = dateFormat.format(calendar.time)
            // Si no existe la key progreso_<fecha>, generamos un valor visual (solo para pruebas)
            if (!prefs.contains("progreso_$fecha")) {
                // Genera progreso entre 20 a 110 (si >100 lo recortamos al 100)
                val progreso = (20 + random.nextInt(90)).coerceAtMost(100)
                // Distribuimos pasos aproximados en función del objetivo
                val pasosAprox = (objetivoPasos * (progreso / 100.0)).toInt()
                val distanciaAprox = (pasosAprox * 0.6) / 1000.0
                val caloriasAprox = (pasosAprox * 0.04).toInt()

                prefs.edit()
                    .putInt("progreso_$fecha", progreso)
                    .putInt("pasos_$fecha", pasosAprox)
                    .putFloat("distancia_$fecha", distanciaAprox.toFloat())
                    .putInt("calorias_$fecha", caloriasAprox)
                    .apply()
            }
        }
    }

    private fun cargarHistorial() {
        val dias = mutableListOf<previousDataClass>()
        val hoy = Date()

        for (i in 6 downTo 0) {
            val calendar = Calendar.getInstance().apply { time = hoy; add(Calendar.DAY_OF_MONTH, -i) }
            val fecha = dateFormat.format(calendar.time)
            val mes = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time).capitalize()
            val dia = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val progreso = getPrefs().getInt("progreso_$fecha", 0)
            val color = R.color.pshy
            dias.add(previousDataClass(mes, dia, progreso, color))
        }

        adp = previousAdapter(dias)
        recyclerView.adapter = adp

        adp.setOnItemClickListener { position: Int ->
            val calendar = Calendar.getInstance().apply { time = hoy; add(Calendar.DAY_OF_MONTH, -(6 - position)) }
            val fechaSeleccionada = dateFormat.format(calendar.time)
            cargarDatosDeDia(fechaSeleccionada)
        }
    }

    private fun cargarDatosDeDia(fecha: String) {
        val prefs = getPrefs()
        pasosHoy = prefs.getInt("pasos_$fecha", 0)
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

    private fun cargarGraficaSemanal() {
        val valores = mutableListOf<Float>()
        val hoy = Date()

        for (i in 6 downTo 0) {
            val calendar = Calendar.getInstance().apply { time = hoy; add(Calendar.DAY_OF_MONTH, -i) }
            val fecha = dateFormat.format(calendar.time)
            val pasos = getPrefs().getInt("pasos_$fecha", 0).toFloat()
            valores.add(pasos)
        }

        val entries = valores.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
        val dataSet = BarDataSet(entries, "Pasos por día").apply {
            color = Color.parseColor("#ad0000")
            valueTextColor = Color.BLACK
            valueTextSize = 12f
            setDrawValues(false)
        }
        val data = BarData(dataSet)
        data.barWidth = 0.35f
        barChart.data = data

        val dias = arrayOf("L", "M", "M", "J", "V", "S", "D")
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(dias)
        barChart.animateY(900)
        barChart.invalidate()
    }

    private fun actualizarUI() {
        txtPasos.text = pasosHoy.toString()
        txtDistancia.text = String.format("%.2f km", calcularDistancia())
        txtCalorias.text = "${calcularCalorias()} kcal"
        txtPasosTotales.text = "/$objetivoPasos"
        txtPorcentaje.text = "${(pasosHoy.toFloat() / objetivoPasos * 100).toInt()}%"
        txtKcalObjetivo.text = "/ $objetivoCalorias kcal"
        txtDistanceGoal.text = "/ ${String.format("%.1f", objetivoDistancia)} km"

        circular.progressMax = objetivoPasos.toFloat()
        circular.setProgressWithAnimation(pasosHoy.toFloat(),
            circular.progressMax.toLong(), AccelerateDecelerateInterpolator())
        barraProgreso.progress = calcularProgresoTotal().toInt()

        txtRacha.text = obtenerRacha().toString()
        rachaIcon.setAnimation(if (yaSumoRachaHoy()) R.raw.flameonn else R.raw.flameoff)
        if (yaSumoRachaHoy()) rachaIcon.playAnimation()
    }

    private fun calcularDistancia(): Double = (pasosHoy * 0.6) / 1000
    private fun calcularCalorias(): Int = (pasosHoy * 0.04).toInt()

    private fun calcularProgresoTotal(): Double {
        val p1 = pasosHoy / objetivoPasos.toFloat()
        val p2 = calcularDistancia() / objetivoDistancia
        val p3 = calcularCalorias() / objetivoCalorias.toFloat()
        return ((p1 + p2 + p3) / 3f * 100f).coerceAtMost(100.0)
    }

    private fun verificarMetasIndividuales() {
        val fecha = fechaHoy()

        // Detectamos cruce de pasos
        if (pasosPrev < objetivoPasos && pasosHoy >= objetivoPasos) {
            mostrarPopupFelicitacion("¡Objetivo de pasos cumplido!")
        }

        // Distancia
        val distanciaHoy = calcularDistancia()
        if (distanciaPrev < objetivoDistancia && distanciaHoy >= objetivoDistancia) {
            mostrarPopupFelicitacion("¡Objetivo de distancia cumplido!")
        }

        // Calorías
        val caloriasHoy = calcularCalorias()
        if (caloriasPrev < objetivoCalorias && caloriasHoy >= objetivoCalorias) {
            mostrarPopupFelicitacion("¡Objetivo de calorías cumplido!")
        }

        // Actualiza prev para la próxima verificación
        pasosPrev = pasosHoy
        distanciaPrev = distanciaHoy
        caloriasPrev = caloriasHoy
    }


    private fun mostrarPopupFelicitacion(mensaje: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("¡Felicidades!")
            .setMessage(mensaje)
            .setPositiveButton("¡Genial!", null)
            .show()
    }

    private fun verificarMetaTotal() {
        if (calcularProgresoTotal() >= 100 && !yaSumoRachaHoy()) {
            sumarRacha()
        }
    }

    private fun sumarRacha() {
        val prefs = getPrefs()
        val nueva = obtenerRacha() + 1
        prefs.edit()
            .putInt("racha", nueva)
            .putString("ultimaFecha", fechaHoy())
            .putBoolean("sumadoHoy", true)
            .putInt("progreso_${fechaHoy()}", 100)
            .apply()
        txtRacha.text = nueva.toString()
        rachaIcon.setAnimation(R.raw.flameonn)
        rachaIcon.playAnimation()
    }

    private fun yaSumoRachaHoy(): Boolean {
        val prefs = getPrefs()
        return prefs.getString("ultimaFecha", "") == fechaHoy() && prefs.getBoolean("sumadoHoy", false)
    }

    private fun obtenerRacha(): Int = getPrefs().getInt("racha", 0)

    private fun verificarResetPorDia() {
        val prefs = getPrefs()
        val ultima = prefs.getString("ultimaFecha", "")
        val hoy = fechaHoy()

        if (ultima != hoy) {
            val progresoAyer = prefs.getInt("progreso_$ultima", 0)
            val rachaPerdida = obtenerRacha()

            if (progresoAyer < 100 && rachaPerdida > 0) {
                mostrarPopupRachaPerdida(rachaPerdida)
                prefs.edit().putInt("racha", 0).apply()
            } else if (progresoAyer >= 100) {
                prefs.edit().putBoolean("sumadoHoy", false).apply()
            }

            pasosHoy = 0
            pasosIniciales = 0  // Reset para counter
            prefs.edit()
                .putInt("pasosHoy", 0)
                .putString("ultimaFecha", hoy)
                .apply()
        }
    }

    private fun mostrarPopupRachaPerdida(racha: Int) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_racha_perdida, null)
        val medallaImage = dialogView.findViewById<ImageView>(R.id.imgMedalla)
        medallaImage.setImageResource(R.drawable.weight)
        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Racha Perdida")
            .setMessage("Tu racha de $racha días terminó porque no cumpliste el 100% ayer.")
            .setPositiveButton("Entendido", null)
            .show()
    }

    private fun cargarDatosHoy() {
        val prefs = getPrefs()
        pasosHoy = prefs.getInt("pasosHoy", prefs.getInt("pasos_${fechaHoy()}", 0))
        // inicializamos prev para que no muestre popup al iniciar si ya superaste objetivo antes de abrir
        pasosPrev = pasosHoy
        distanciaPrev = prefs.getFloat("distancia_${fechaHoy()}", calcularDistancia().toFloat()).toDouble()
        caloriasPrev = prefs.getInt("calorias_${fechaHoy()}", calcularCalorias())
        actualizarUI()
    }


    private fun guardarDatosHoy() {
        val prefs = getPrefs()
        val fecha = fechaHoy()
        prefs.edit()
            .putInt("pasosHoy", pasosHoy)
            .putInt("pasos_$fecha", pasosHoy)
            .putFloat("distancia_$fecha", calcularDistancia().toFloat())
            .putInt("calorias_$fecha", calcularCalorias())
            .putInt("progreso_$fecha", calcularProgresoTotal().toInt())
            .putString("ultimaFecha", fecha)
            .apply()
    }


    private fun fechaHoy(): String = dateFormat.format(Date())

    private fun getPrefs() = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
