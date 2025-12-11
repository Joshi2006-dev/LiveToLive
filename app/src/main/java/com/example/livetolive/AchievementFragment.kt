package com.example.livetolive

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    var l1=false
    var l2=false
    var l3=false
    var l4=false
    var l5=false
    var l6=false




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
        val nAchivs=achivement.findViewById<TextView>(R.id.txtCounterAchicements)

        if (sharedPreferencesApp.getInt("logrosObtenido",0)>=1){
            l1=true
        }
        if(sharedPreferencesApp.getInt("logrosObtenido",0)>=10){
            l2=true
        }

        if(sharedPreferencesApp.getInt("logrosObtenido",0)>=50){
            l3=true
        }

        if (sharedPreferencesApp.getInt("logrosObtenido",0)>=100){
            l4=true
        }

        if (sharedPreferencesApp.getInt("logrosObtenido",0)>=500) {
            l5 = true
        }

        if (sharedPreferencesApp.getInt("logrosObtenido",0)>=1000){
            l6=true
        }


        //Declaracion del popup
        val dialogView=layoutInflater.inflate(R.layout.achivementpopup,null)
        val btnReclamar=dialogView.findViewById<Button>(R.id.btnReclamar)
        val popupIcon=dialogView.findViewById<LottieAnimationView>(R.id.AchivementIcon)
        val TituloPopu=dialogView.findViewById<TextView>(R.id.txtNameAchivement)
        val descPopup=dialogView.findViewById<TextView>(R.id.descAchivement)
        val dialog= AlertDialog.Builder(requireContext()).setView(dialogView).create()

        btnReclamar.setOnClickListener{
            dialog.dismiss()
        }


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
        if(l1){
            CardBronze.setBackgroundResource(R.drawable.bronzeback)
            tittleBronze.text="BRONCE"
            tittleBronze.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            descBronze.text="Completa cualquier objetivo por primera vez"
            bronzeIcon.setAnimation(R.raw.bronze)
            bronzeIcon.scaleY=2f
            bronzeIcon.scaleX=2f
            bronzeIcon.playAnimation()
            if(!sharedPreferencesApp.getBoolean("BronzPop")){
                TituloPopu.text= tittleBronze.text
                descPopup.text=descBronze.text
                popupIcon.setAnimation(R.raw.bronze)
                popupIcon.scaleY=2f
                popupIcon.scaleX=2f
                dialog.show()
                sharedPreferencesApp.saveBoolean("BronzPop",true)
            }
            nAchivs.text="Obtenidos (1/6)"
        }


        //Silver Achivement Unlock
        if(l2){
            CardSilver.setBackgroundResource(R.drawable.silverback)
            tittleSilver.text="PLATA"
            tittleSilver.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            descSilver.text="Completa 10 objetivos"
            silverIcon.setAnimation(R.raw.silver)
            silverIcon.scaleY=2f
            silverIcon.scaleX=2f
            silverIcon.playAnimation()
            if(!sharedPreferencesApp.getBoolean("SilverPop")){
                TituloPopu.text= tittleSilver.text
                descPopup.text=descSilver.text
                popupIcon.setAnimation(R.raw.silver)
                popupIcon.scaleY=2f
                popupIcon.scaleX=2f
                dialog.show()
                sharedPreferencesApp.saveBoolean("SilverPop",true)
            }
            nAchivs.text="Obtenidos (2/6)"
        }


        //Gold Achivement Unlock
        if (l3){
            CardGold.setBackgroundResource(R.drawable.goldback)
            tittleGold.text="ORO"
            tittleGold.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            descGold.text="Completa 50 objetivos"
            goldIcon.setAnimation(R.raw.gold)
            goldIcon.playAnimation()
            if(!sharedPreferencesApp.getBoolean("GoldPop")){
                TituloPopu.text= tittleGold.text
                descPopup.text=descGold.text
                popupIcon.scaleY=1f
                popupIcon.scaleX=1f
                popupIcon.setAnimation(R.raw.gold)
                dialog.show()
                sharedPreferencesApp.saveBoolean("GoldPop",true)
            }
            nAchivs.text="Obtenidos (3/6)"
        }


        //Platinum Achivement Unlock
        if(l4){
            CardPlatinum.setBackgroundResource(R.drawable.platinumback)
            tittlePlat.text="PLATINO"
            tittlePlat.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            descPlatinun.text="Completa 100 objetivos"
            platinumIcon.setAnimation(R.raw.platinum)
            platinumIcon.playAnimation()
            platinumIcon.speed=1.5f
            if(!sharedPreferencesApp.getBoolean("PlatinumPop")){
                TituloPopu.text= tittlePlat.text
                descPopup.text=descPlatinun.text
                popupIcon.scaleY=1f
                popupIcon.scaleX=1f
                popupIcon.speed=1.7f
                popupIcon.setAnimation(R.raw.platinum)
                dialog.show()
                sharedPreferencesApp.saveBoolean("PlatinumPop",true)
            }
            nAchivs.text="Obtenidos (4/6)"
        }



        //Diamond Achivement Unlock
        if (l5){
            CardDiamond.setBackgroundResource(R.drawable.hidratationgradient)
            tittleDiam.text="DIAMANTE"
            tittleDiam.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            descDiamond.text="Completa 200 objetivos"
            diamondIcon.setAnimation(R.raw.diamond)
            diamondIcon.updatePadding(0,0,0,30)
            diamondIcon.scaleX=1.6f
            diamondIcon.scaleY=1.6f
            if(!sharedPreferencesApp.getBoolean("DiamondPop")){
                TituloPopu.text= tittleDiam.text
                descPopup.text=descDiamond.text
                popupIcon.scaleY=1.6f
                popupIcon.scaleX=1.6f
                popupIcon.speed=1f
                popupIcon.updatePadding(0,0,0,30)
                popupIcon.setAnimation(R.raw.diamond)
                dialog.show()
                sharedPreferencesApp.saveBoolean("DiamondPop",true)
            }
            nAchivs.text="Obtenidos (5/6)"
        }



        //King Achivement Unlock
        if(l6){
            cardKing.setBackgroundResource(R.drawable.kingachback)
            titleKing.text="REY DE LA DISCIPLINA"
            titleKing.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            descKing.text="Completa 1000 objetivos"
            Icono.setAnimation(R.raw.masterachicon)
            Icono.updatePadding(0,0,0,0)
            if(!sharedPreferencesApp.getBoolean("KingPop")){
                TituloPopu.text= titleKing.text
                descPopup.text=descKing.text
                popupIcon.scaleY=1f
                popupIcon.scaleX=1f
                popupIcon.speed=1f
                popupIcon.updatePadding(0,0,0,0)
                popupIcon.setAnimation(R.raw.masterachicon)
                dialog.show()
                sharedPreferencesApp.saveBoolean("KingPop",true)
            }
            nAchivs.text="Obtenidos (6/6)"
        }
        //El popup



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