package com.example.basededatos

// Importaciones necesarias para trabajar con Room y Android
import android.content.Context
import androidx.room.Room

// Objeto singleton que gestiona la base de datos
object DatabaseManager {

    // Variable privada que almacena la instancia única de la base de datos
    private var appDatabase: AppDatabase? = null

    // Función que devuelve la instancia de la base de datos
    fun getDatabase(context: Context): AppDatabase {
        // Verificar si la base de datos ya ha sido inicializada
        if (appDatabase == null) {
            // Si no ha sido inicializada, crear una nueva instancia de la base de datos
            appDatabase = Room.databaseBuilder(
                context.applicationContext, // Contexto de la aplicación
                AppDatabase::class.java, // Clase que representa la base de datos (AppDatabase en este caso)
                "database-name" // Nombre de la base de datos
            ).build() // Construir la base de datos
        }
        // Devolver la instancia de la base de datos (puede ser la nueva instancia o la existente)
        return appDatabase!!
    }
}