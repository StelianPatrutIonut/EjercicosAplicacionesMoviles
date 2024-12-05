package com.example.intentimplicito

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.intentimplicito.databinding.ActivityMainBinding
import com.example.intentimplicito.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button6.setOnClickListener{
// This is used send the custom data with the result.
            val intent = Intent()
            intent.putExtra(MainActivity.NAME, binding.name.text.toString())
            intent.putExtra(MainActivity.EMAIL, binding.email.text.toString())

            setResult(Activity.RESULT_OK, intent) // It is used to set the RESULT OK and a custom data values which we wants to send back.
            finish()
        }
    }
}