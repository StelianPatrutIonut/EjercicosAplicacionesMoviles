package com.example.appdefinitiva10

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class juegoResultado(
    @PrimaryKey val nombre : String,
    @ColumnInfo var puntuacionAcumulada: Int,
    @ColumnInfo var puntuacionMaxima : Int
)
