package com.example.recyclersample.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclersample.R

class MainFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FragmentActivity", "onCreate()")
        setContentView(R.layout.fragment_main_activity)
        val btnChangeColor = findViewById<View>(R.id.btn_random_color)
        btnChangeColor.setOnClickListener {
            changeFragmentColor()
        }
    }

    private fun changeFragmentColor() {
        val color = arrayOf(
            R.color.design_default_color_background,
            R.color.green,
            R.color.colorAccent,
            R.color.cardview_light_background,
            R.color.white
        )
        val fragment1 = supportFragmentManager.findFragmentById(R.id.fragment1)
        fragment1?.view?.setBackgroundColor(color.random())
    }


}