package com.example.basededatos

// Importaciones necesarias para trabajar con Android y coroutines
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Clase principal que representa la actividad MainActivity
class MainActivity : AppCompatActivity() {

    // Declaración de la variable del botón
    lateinit var button: Button

    // Función llamada cuando se crea la actividad
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecer el diseño de la actividad desde el archivo de diseño activity_main.xml
        setContentView(R.layout.activity_main)

        // Inicializar la variable del botón
        button = findViewById(R.id.button)

        // Usar coroutines para realizar operaciones asincrónicas
        lifecycleScope.launch {

            // Utilizar Dispatchers.IO para realizar operaciones de E/S en un hilo de fondo
            withContext(Dispatchers.IO) {

                // Obtener la instancia de la base de datos a través del DatabaseManager
                val db = DatabaseManager.getDatabase(applicationContext)

                // Obtener una instancia del UserDao desde la base de datos
                val userDao = db.userDao()

                // Verificar si ya existen usuarios en la base de datos
                val existingUsers = userDao.getAll()

                // Insertar algunos datos de prueba solo si la base de datos está vacía
                if (existingUsers.isEmpty()) {
                    userDao.insertAll(
                        User(1, "John", "Doe"),
                        User(2, "Jane", "Doe")
                    )
                }
            }

            // Obtener y mostrar todos los usuarios desde MainActivity
            val users = withContext(Dispatchers.IO) {
                val db = DatabaseManager.getDatabase(applicationContext)
                val userDao = db.userDao()
                userDao.getAll()
            }

            // Imprimir en el registro (Log) la lista de usuarios obtenida
            Log.d("Miguel", "users in MainActivity: $users")
        }

        // Configurar un listener para el botón
        button.setOnClickListener {
            // Crear una intención para iniciar la actividad MainActivity2
            val intent = Intent(this, MainActivity2::class.java)

            // Iniciar la actividad MainActivity2
            startActivity(intent)
        }
    }
}