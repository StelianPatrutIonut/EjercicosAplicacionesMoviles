package com.example.secondintentweb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        var web1: WebView? = null
        var button: Button? = null
        web1 = findViewById(R.id.web1)
        val bundle = intent.extras
        val dato = bundle!!.getString("direccion")
        web1.loadUrl("https://$dato")


    }


}