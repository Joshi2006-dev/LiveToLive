package com.example.livetolive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SleepFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SleepFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var recyclerView: RecyclerView
    private lateinit var adp: previousAdapter
    private val datesList=listOf(
        previousDataClass("Noviembre","27",50,R.color.sleep),
        previousDataClass("Noviembre","28",90,R.color.sleep),
        previousDataClass("Noviembre","29",70,R.color.sleep),
        previousDataClass("Noviembre","30",100,R.color.sleep),
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
        val sleep= inflater.inflate(R.layout.fragment_sleep, container, false)
        val scroller=sleep.findViewById<ScrollView>(R.id.sleepScroll)
        val sleepProgress=sleep.findViewById<ProgressBar>(R.id.progressBarSleep)
        val anBar= barAnimation()
        anBar.animateProgress(sleepProgress,0,37)
        recyclerView=sleep.findViewById(R.id.historial)
        recyclerView.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        adp= previousAdapter(datesList)
        recyclerView.adapter=adp
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
        return sleep
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SleepFragment.
         */
        // TODO: Rename and change types and number of parameters
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