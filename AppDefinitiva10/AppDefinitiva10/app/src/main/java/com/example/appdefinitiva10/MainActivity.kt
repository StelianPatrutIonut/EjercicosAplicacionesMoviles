package com.example.appdefinitiva10

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import com.example.appdefinitiva10.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var boton = binding.Registro
        boton.setOnClickListener{
            var intent = Intent(this,Registro::class.java)
            startActivity(intent)
        }
        runnable = Runnable {
            var intent = Intent(this,Administrador::class.java)
            startActivity(intent)
        }

        var boton2 = binding.Login
        boton2.setOnClickListener{
            var intent = Intent(this,Login::class.java)
            startActivity(intent)
        }

        handler = Handler(Looper.getMainLooper())

        binding.admin.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN ->{
                    handler.postDelayed(runnable,3000)
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->{
                    handler.removeCallbacks(runnable)
                    true
                }
                else ->false
            }
        }

    }
}