package com.example.databinder



import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.databinder.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var CLAVE_INFO = "PRIMERACOMUNICACION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonToast.setOnClickListener { toast() }
        val color = Color.argb(255, 255, 156, 56)
        binding.usuario.setBackgroundColor(color)

        binding.lanzador.setOnClickListener(View.OnClickListener { lanzar() })


    }

    private fun lanzar(){


        var intent = Intent(this, Reception::class.java)
       // print("asdf")
        intent.putExtra("PRIMERA", binding.usuario.getText().toString());
        startActivity(intent);


    }
    private fun toast(){
           Toast.makeText(this,"TOASSSST",Toast.LENGTH_LONG).show()


    }
}
