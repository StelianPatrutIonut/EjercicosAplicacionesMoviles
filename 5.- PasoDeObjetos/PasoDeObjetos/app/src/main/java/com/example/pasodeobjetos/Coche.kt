package com.example.pasodeobjetos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coche(val marca: String, val modelo: String) : Parcelable