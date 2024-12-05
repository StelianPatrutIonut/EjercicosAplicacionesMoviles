package com.example.databinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.databinder.databinding.ActivityReceptionBinding
import com.example.databinder.databinding.ActivityReceptorBinding

class Reception : AppCompatActivity() {

    lateinit var mensaje: EditText
    // var recoger: Bundle? = null
    //lateinit var binding1: ActivityReceptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reception)
        mensaje = findViewById(R.id.texto)
      //  binding1 = ActivityReceptionBinding.inflate(layoutInflater)

        var recoger: String? = getIntent().getStringExtra("PRIMERA")
        mensaje.setText(recoger)
      //  binding1.recibido.setText(recoger)
    }
}