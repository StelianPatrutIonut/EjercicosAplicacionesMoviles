package com.example.appdefinitiva10

import android.os.Bundle
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appdefinitiva10.databinding.ActivityFirebaseGoogleBinding
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseGoogle : AppCompatActivity() {
    private lateinit var datas: ActivityFirebaseGoogleBinding
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datas = ActivityFirebaseGoogleBinding.inflate(layoutInflater)
        setContentView(datas.root)
        val usuario = intent.getStringExtra("username")
        datas.usuarioFirebase.text = usuario // Utilizando ViewBinding
        val avatarByteArray = intent.getByteArrayExtra("avatar")
        if (avatarByteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.size)
            datas.avatarFirebase.setImageBitmap(bitmap) // Utilizando ViewBinding
        } else {
            Log.d("FirebaseGoogle", "Avatar byte array is null")
        }
        datas.buttonAgregarAlumno.setOnClickListener {
            insertarDatos()
        }
    }

    private fun insertarDatos() {
        // Obtener valores desde los EditText
        val nombre = datas.editTextNombre.text.toString()
        val apellidos = datas.editTextApellido.text.toString()
        val nifIntroducido = datas.editTextNIF.text.toString()
        val niaIntroducido = datas.editTextNIA.text.toString()
        val mediaExpedienteStr = datas.editTextMediaExpediente.text.toString()

        // Convertir a Double de manera segura
        val mediaExpediente = mediaExpedienteStr.toDoubleOrNull()

        // Verificar campos vacíos o que la conversión a Double fue exitosa
        if (nombre.isEmpty() || apellidos.isEmpty() || nifIntroducido.isEmpty() || niaIntroducido.isEmpty() || mediaExpediente == null) {
            Toast.makeText(this, "Todos los campos son obligatorios y la media debe ser un número", Toast.LENGTH_SHORT).show()
            return
        }

        val collectionRef = firestore.collection("alumnosSIP")

        // Verificar duplicados por NIA
        collectionRef.whereEqualTo("nia", niaIntroducido).get().addOnSuccessListener { documentsNia ->
            if (!documentsNia.isEmpty) {
                //Toast.makeText(this, "Ya existe un alumno con este NIA", Toast.LENGTH_SHORT).show()
                AlertDialog.Builder(this)
                    .setTitle("Alumno con NIA EXISTENTE")
                    .setMessage("Ya existe un alumno con este NIA, prueba con otro")
                    .setPositiveButton("OK",null)
                    .show()
            } else {
                // Verificar duplicados por NIF
                collectionRef.whereEqualTo("nif", nifIntroducido).get().addOnSuccessListener { documentsNif ->
                    if (!documentsNif.isEmpty) {
                      //  Toast.makeText(this, "Ya existe un alumno con este NIF", Toast.LENGTH_SHORT).show()
                        AlertDialog.Builder(this)
                            .setTitle("Alumno con NIF EXISTENTE")
                            .setMessage("Ya existe un alumno con este NIF, prueba con otro")
                            .setPositiveButton("OK",null)
                            .show()
                    } else {
                        val nuevoAlumno = hashMapOf(
                            "nif" to nifIntroducido,
                            "nia" to niaIntroducido,
                            "nombre" to nombre,
                            "apellido" to apellidos,
                            "mediaexpediente" to mediaExpediente // Asegúrate de que sea el valor Double
                        )
                        collectionRef.add(nuevoAlumno).addOnSuccessListener {
                            Toast.makeText(this, "Documento agregado correctamente", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "Error al agregar el documento: $e", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Error al verificar duplicados: $e", Toast.LENGTH_SHORT).show()
        }
    }

}
