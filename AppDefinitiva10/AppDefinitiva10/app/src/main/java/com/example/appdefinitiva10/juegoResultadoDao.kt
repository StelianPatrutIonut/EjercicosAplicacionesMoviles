package com.example.appdefinitiva10

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface juegoResultadoDao {
    @Insert
    fun insert(juegoResultado: juegoResultado)
    @Query(" SELECT * FROM juegoResultado WHERE nombre = :nombreUsuario")
    fun getJuegoResultado(nombreUsuario : String): juegoResultado?
    @Update
    fun update(juegoResultado: juegoResultado)
    @Query("DELETE FROM juegoResultado")
    fun deleteAllUsers()

}