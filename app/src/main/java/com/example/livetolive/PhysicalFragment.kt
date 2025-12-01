package com.example.livetolive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.mikhaellopez.circularprogressbar.CircularProgressBar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhysicalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhysicalFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adp: previousAdapter
    private val datesList=listOf(
        previousDataClass("Noviembre","27",50,R.color.pshy),
        previousDataClass("Noviembre","28",90,R.color.pshy),
        previousDataClass("Noviembre","29",70,R.color.pshy),
        previousDataClass("Noviembre","30",100,R.color.pshy),
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
        val phy= inflater.inflate(R.layout.fragment_physical, container, false)
        val scroller=phy.findViewById<ScrollView>(R.id.physicalScroll)
        val phyProgress=phy.findViewById<ProgressBar>(R.id.progressBarPhy)
        val phyPasosProgression=phy.findViewById<CircularProgressBar>(R.id.circularProgressBar)
        val anBar= barAnimation()
        val contador=phy.findViewById<Button>(R.id.btnContador)
        val racha=phy.findViewById<LottieAnimationView>(R.id.imgStreak)
        recyclerView=phy.findViewById(R.id.historial)
        recyclerView.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        adp= previousAdapter(datesList)
        recyclerView.adapter=adp
        recyclerView.scrollToPosition(adp.itemCount - 1)
        racha.setAnimation(R.raw.flameoff)
        racha.playAnimation()
        var active=false


        anBar.animateProgress(phyProgress,0,69)
        scroller.apply {
            translationY = -100f
            alpha = 0f

            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500)
                .start()
        }

        contador.setOnClickListener {
            active=streakAnimation(racha,R.raw.flameonn,active)
        }

        phyPasosProgression.apply {
            setProgressWithAnimation(
                progress = 1790f,
                duration = 1000,
                interpolator = AccelerateDecelerateInterpolator()
            )
        }

        return phy
    }

    private fun  streakAnimation(imageView: LottieAnimationView, animation: Int, act: Boolean): Boolean{
        if(!act){
            imageView.setAnimation(animation)
            imageView.playAnimation()
        }else{
            imageView.setAnimation(R.raw.flameoff)
            imageView.playAnimation()
        }

        return !act
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhysicalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhysicalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}