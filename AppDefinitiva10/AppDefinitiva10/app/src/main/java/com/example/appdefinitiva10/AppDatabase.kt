package com.example.appdefinitiva10

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [User::class, juegoResultado::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Método abstracto que devuelve el DAO de usuario
    abstract fun userDao(): UserDao
    abstract fun juegoResultadoDao() : juegoResultadoDao
}
