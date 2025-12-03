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
        //Para el logro 1
        val CardBronze=achivement.findViewById<RelativeLayout>(R.id.CardLogro1)
        val tittleBronze=achivement.findViewById<TextView>(R.id.txtLogro1)
        val descBronze=achivement.findViewById<TextView>(R.id.txtLogroDesc1)
        val bronzeIcon=achivement.findViewById<LottieAnimationView>(R.id.achBronze)


        //Para el logro 2
        val CardSilver=achivement.findViewById<RelativeLayout>(R.id.CardLogro2)
        val tittleSilver=achivement.findViewById<TextView>(R.id.txtLogro2)
        val descSilver=achivement.findViewById<TextView>(R.id.txtLogroDesc2)
        val silverIcon=achivement.findViewById<LottieAnimationView>(R.id.achSilver)

        //Para el logro 3
        val CardGold=achivement.findViewById<RelativeLayout>(R.id.CardLogro3)
        val tittleGold=achivement.findViewById<TextView>(R.id.txtLogro3)
        val descGold=achivement.findViewById<TextView>(R.id.txtLogroDesc3)
        val goldIcon=achivement.findViewById<LottieAnimationView>(R.id.achGold)

        //Para el logro 4
        val CardPlatinum=achivement.findViewById<RelativeLayout>(R.id.CardLogro4)
        val tittlePlat=achivement.findViewById<TextView>(R.id.txtLogro4)
        val descPlatinun=achivement.findViewById<TextView>(R.id.txtLogroDesc4)
        val platinumIcon=achivement.findViewById<LottieAnimationView>(R.id.achPlatinum)

        //Para el logro 5
        val CardDiamond=achivement.findViewById<RelativeLayout>(R.id.CardLogro5)
        val tittleDiam=achivement.findViewById<TextView>(R.id.txtLogro5)
        val descDiamond=achivement.findViewById<TextView>(R.id.txtLogroDesc5)
        val diamondIcon=achivement.findViewById<LottieAnimationView>(R.id.achDiamond)
        //Para el logro 6
        val cardKing=achivement.findViewById<RelativeLayout>(R.id.CardLogro6)
        val titleKing=achivement.findViewById<TextView>(R.id.txtLogro6)
        val descKing=achivement.findViewById<TextView>(R.id.txtLogroDesc6)
        val Icono=achivement.findViewById<LottieAnimationView>(R.id.achKing)



        //Bronze Achivement Unlock
        CardBronze.setBackgroundResource(R.drawable.bronzeback)
        tittleBronze.text="BRONCE"
        tittleBronze.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        descBronze.text="Completa cualquier objetivo por primera vez"
        bronzeIcon.setAnimation(R.raw.bronze)
        bronzeIcon.scaleY=2f
        bronzeIcon.scaleX=2f
        bronzeIcon.playAnimation()

        //Silver Achivement Unlock
        CardSilver.setBackgroundResource(R.drawable.silverback)
        tittleSilver.text="PLATA"
        tittleSilver.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        descSilver.text="Completa 50 objetivos"
        silverIcon.setAnimation(R.raw.silver)
        silverIcon.scaleY=2f
        silverIcon.scaleX=2f
        silverIcon.playAnimation()

        //Gold Achivement Unlock
        CardGold.setBackgroundResource(R.drawable.goldback)
        tittleGold.text="ORO"
        tittleGold.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        descGold.text="Completa 50 objetivos"
        goldIcon.setAnimation(R.raw.gold)
        goldIcon.playAnimation()

        //Platinum Achivement Unlock
        CardPlatinum.setBackgroundResource(R.drawable.platinumback)
        tittlePlat.text="PLATINO"
        tittlePlat.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        descPlatinun.text="Completa 100 objetivos"
        platinumIcon.setAnimation(R.raw.platinum)
        platinumIcon.playAnimation()
        platinumIcon.speed=1.5f


        //Diamond Achivement Unlock
        CardDiamond.setBackgroundResource(R.drawable.hidratationgradient)
        tittleDiam.text="DIAMANTE"
        tittleDiam.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        descDiamond.text="Completa 500 objetivos"
        diamondIcon.setAnimation(R.raw.diamond)
        diamondIcon.updatePadding(0,0,0,30)
        diamondIcon.scaleX=1.6f
        diamondIcon.scaleY=1.6f


        //King Achivement Unlock
        cardKing.setBackgroundResource(R.drawable.kingachback)
        titleKing.text="REY DE LA DISCIPLINA"
        titleKing.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        descKing.text="Completa 1000 objetivos"
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