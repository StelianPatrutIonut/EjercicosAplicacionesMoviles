package com.example.pasodeobjetos

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pasodeobjetos.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {

            val car = Car("Seat", "Toledo")
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            intent.putExtra("car",car)
            startActivity(intent)
        }
        binding.button2.setOnClickListener {
            val coche = Coche("Ford", "Fiesta")
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            intent.putExtra("coche", coche)
            startActivity(intent)

        }
    }
}




