package com.example.arqmvc_v0.Model

import com.google.android.material.textfield.TextInputLayout.LengthCounter

class MyModel {
    private var counter = 0
    private var counter2 = 0
    private var counter3 = 0
    private var counter4 = 0


    fun incrementCounter() {
        counter++

    }

    fun getCounter(): Int {
        return counter

    }
    fun incrementCounter1() {
        counter2++

    }

    fun getCounter1(): Int {
        return counter2

    }
    fun incrementCounter2() {
        counter3++

    }

    fun getCounter2(): Int {
        return counter3

    }
    fun incrementCounter3() {
        counter4++

    }

    fun getCounter3(): Int {
        return counter4

    }
}