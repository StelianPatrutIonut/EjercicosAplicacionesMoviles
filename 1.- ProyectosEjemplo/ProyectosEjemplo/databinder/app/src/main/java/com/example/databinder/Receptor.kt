package com.example.databinder

import android.R
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.databinder.databinding.ActivityReceptorBinding

class Receptor : AppCompatActivity() {
    var mensaje: TextView? = null
   // var recoger: Bundle? = null
    private lateinit var binding: ActivityReceptorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_receptor)
      //  binding1 = ActivityReceptorBinding.inflate(layoutInflater)

        //var cosa: String
        var recoger: String? = getIntent().getStringExtra("PRIMERA")

        binding.mensaje.text = "abc"


    }


}