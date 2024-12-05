package com.example.basededatos

// Importaciones necesarias para trabajar con Android y coroutines
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Clase principal que representa la actividad MainActivity2
class MainActivity2 : AppCompatActivity() {

    // Función llamada cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecer el diseño de la actividad desde el archivo de diseño activity_main2.xml
        setContentView(R.layout.activity_main2)

        // Usar coroutines para realizar operaciones asincrónicas
        lifecycleScope.launch {

            // Usar Dispatchers.IO para realizar operaciones de E/S en un hilo de fondo
            val users = withContext(Dispatchers.IO) {

                // Obtener la instancia de la base de datos a través del DatabaseManager
                val db = DatabaseManager.getDatabase(applicationContext)

                // Obtener una instancia del UserDao desde la base de datos
                val userDao = db.userDao()

                // Obtener todos los usuarios desde el UserDao de manera asíncrona
                userDao.getAll()
            }

            // Imprimir en el registro (Log) la lista de usuarios obtenida
            Log.d("Miguel2", "users in MainActivity2: $users")

        }
    }
}