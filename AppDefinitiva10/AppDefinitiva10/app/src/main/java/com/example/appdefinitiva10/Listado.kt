package com.example.appdefinitiva10

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.appdefinitiva10.databinding.ActivityListadoBinding
import com.example.appdefinitiva10.databinding.ActivityRegistroBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class Listado : AppCompatActivity() {
    private lateinit var binding : ActivityListadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            // Obtener la lista de usuarios desde la base de datos en un hilo de fondo
            val users = withContext(Dispatchers.IO) {
                DatabaseManager.getDatabase(applicationContext).userDao().getAll()
            }

            // Iterar sobre la lista de usuarios y mostrar la información en la interfaz de usuario
            users.forEach { user ->
                // Inflar el diseño de un elemento de la lista de usuarios
                val itemView = layoutInflater.inflate(R.layout.activity_listado, null, false)

                // Encontrar las vistas en el diseño del elemento de la lista
                val avatarImageView = itemView.findViewById<ImageView>(R.id.avatar)
                val usernameTextView = itemView.findViewById<TextView>(R.id.username)
                val fechaDeNacimientoUsuarioTextView = itemView.findViewById<TextView>(R.id.fechaDeNacimientoUsuario)

                // Mostrar avatar (si está disponible)
                user.avatar?.let {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    avatarImageView.setImageBitmap(bitmap)
                }

                // Mostrar nombre de usuario y fecha de nacimiento
                usernameTextView.text = user.userName
                fechaDeNacimientoUsuarioTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(user.fechaNacimiento)

                // Agregar el elemento de la lista al layout principal
                binding.linearLayoutUsuarios.addView(itemView)
            }
        }
    }
}
