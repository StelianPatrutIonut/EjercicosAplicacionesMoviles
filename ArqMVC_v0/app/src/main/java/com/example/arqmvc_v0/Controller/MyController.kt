package com.example.arqmvc_v0.Controller

import android.view.View
import com.example.arqmvc_v0.Model.MyModel
import com.example.arqmvc_v0.View.MyView
import com.example.arqmvc_v0.View.MyViewLogin

class MyController(private val model: MyModel, private val view: MyView,private val view2: MyViewLogin) {

    init {
        view.setButton1ClickListener {
            model.incrementCounter()
            model.incrementCounter1()
            updateView()
        }

        view.setButton2ClickListener {
            model.incrementCounter()
            model.incrementCounter2()
            updateView()
        }

        view.setButton3ClickListener {
            model.incrementCounter()
            model.incrementCounter3()
            updateView()
        }

        // Actualizar la vista inicialmente
        updateView()
    }

    private fun updateView() {
        val counterValue = model.getCounter()
        val counterValue2 = model.getCounter1()
        val counterValue3 = model.getCounter2()
        val counterValue4 = model.getCounter3()

        view.setCounterText(counterValue,counterValue2,counterValue3,counterValue4)
    }
}