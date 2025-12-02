package com.example.livetolive

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var  navegacion: BottomNavigationView
    private var seleccion= BottomNavigationView.OnNavigationItemSelectedListener{
            item->
        when(item.itemId){
            R.id.home->{
                supportFragmentManager.commit {
                    replace<HomeFragment>(R.id.Frame)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                true
            }
            R.id.achievement->{
                true
            }
            R.id.group->{
                true
            }
            R.id.profile->{
                supportFragmentManager.commit {
                    replace<ProfileFragment>(R.id.Frame)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                true
                true
            }
            else->{
                false
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navegacion=findViewById(R.id.NaviMenu)
        navegacion.setOnNavigationItemSelectedListener(seleccion)
        supportFragmentManager.commit {
            replace<HomeFragment>(R.id.Frame)
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }

    }
}