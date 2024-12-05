package com.example.secondintentweb

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var et1: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main);


        et1=findViewById(R.id.et1);

    }

    fun ver(v: View?) {
        val i = Intent(this, MainActivity2::class.java)
        i.putExtra("direccion", et1!!.text.toString())
        startActivity(i)
    }

}