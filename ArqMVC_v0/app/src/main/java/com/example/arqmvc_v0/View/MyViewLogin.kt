package com.example.arqmvc_v0.View

import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arqmvc_v0.R

class MyViewLogin(private val activity2: AppCompatActivity) {
    private val usuario : TextView=activity2.findViewById(R.id.usuario)
    private val contrasenia : TextView=activity2.findViewById(R.id.contrasenia)
    private val boton : Button = activity2.findViewById(R.id.boton4)

    init {
        // Asigna un OnClickListener al botón
        boton.setOnClickListener {
            // Obtiene el texto del TextView del usuario y la contraseña
            val usuarioText = usuario.text.toString()

            // Verifica si el usuario ha ingresado la información requerida
            if (usuarioText.isNotEmpty()) {
                // Crea un Intent para pasar a la siguiente pantalla
                val intent = Intent(activity2, MyView::class.java)

                // Agrega el nombre de usuario como "extra" en el Intent
                intent.putExtra("usuario", usuarioText)

                // Inicia la nueva actividad
                activity2.startActivity(intent)
            } else {
                // Muestra un mensaje de error si no se ha ingresado el nombre de usuario
                Toast.makeText(activity2, "Por favor, ingrese el nombre de usuario", Toast.LENGTH_SHORT).show()
            }
        }
        }
    }


