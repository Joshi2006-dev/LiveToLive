package com.example.livetolive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        val sleepProgress = sleep.findViewById<ProgressBar>(R.id.progressBarSleep)

        val anBar = barAnimation()
        anBar.animateProgress(sleepProgress, 0, 37)


        recyclerView = sleep.findViewById(R.id.historial)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adp = previousAdapter(datesList)
        recyclerView.adapter = adp
        recyclerView.scrollToPosition(adp.itemCount - 1)

        scroller.apply {
            translationY = -100f
            alpha = 0f
            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500)
                .start()
        }

        // Boton para abiri el boottomshett de la alarma
        val btnSetAlarm = sleep.findViewById<Button>(R.id.btnSetAlarm)
        btnSetAlarm.setOnClickListener {
            val bottom = AlarmBottomSheet()
            bottom.show(requireActivity().supportFragmentManager, "alarmBottom")
        }

        return sleep
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