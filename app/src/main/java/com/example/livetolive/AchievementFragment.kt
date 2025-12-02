package com.example.livetolive

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.airbnb.lottie.LottieAnimationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AchievementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AchievementFragment : Fragment() {
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
        val achivement= inflater.inflate(R.layout.fragment_achievement, container, false)
        val scroller=achivement.findViewById<ScrollView>(R.id.scroller)
        //Para el logro 6
        val cardKing=achivement.findViewById<RelativeLayout>(R.id.CardLogro6)
        val titleKing=achivement.findViewById<TextView>(R.id.txtLogro6)
        val descKing=achivement.findViewById<TextView>(R.id.txtLogroDesc6)
        val Icono=achivement.findViewById<LottieAnimationView>(R.id.achKing)

        cardKing.setBackgroundResource(R.drawable.kingachback)
        titleKing.text="REY DE LA DISCIPLINA"
        titleKing.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        descKing.text="Completa 100 objetivos"
        Icono.setAnimation(R.raw.masterachicon)
        Icono.updatePadding(0,0,0,0)
        scroller.apply {
            translationY = -100f
            alpha = 0f

            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500)
                .start()
        }

        return achivement
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AchievementFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AchievementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}