package com.example.arqmvc_v0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.arqmvc_v0.Controller.MyController
import com.example.arqmvc_v0.Model.MyModel
import com.example.arqmvc_v0.View.MyView
import com.example.arqmvc_v0.View.MyViewLogin

class MainActivity : AppCompatActivity() {

    private lateinit var model: MyModel
    private lateinit var view: MyView
    private lateinit var view2: MyViewLogin
    private lateinit var controller: MyController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity2)

        model = MyModel()
        view = MyView(this)
        view2 = MyViewLogin(this)
        controller = MyController(model, view,view2)
    }

}