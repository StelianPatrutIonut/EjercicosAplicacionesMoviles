package com.example.basededatos

import androidx.room.Database
import androidx.room.RoomDatabase

// Anotación que indica que esta clase es una base de datos Room
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Método abstracto que devuelve el DAO de usuario
    abstract fun userDao(): UserDao
}
