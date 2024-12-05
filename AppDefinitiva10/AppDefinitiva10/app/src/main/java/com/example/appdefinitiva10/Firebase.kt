package com.example.appdefinitiva10

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.appdefinitiva10.databinding.ActivityFirebaseBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class Firebase : AppCompatActivity() {
    private lateinit var datas : ActivityFirebaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datas = ActivityFirebaseBinding.inflate(layoutInflater)
        setContentView(datas.root)
        val db = FirebaseFirestore.getInstance()
        val alumnosRef = db.collection("alumnosSIP")
        datas.listado.visibility = View.INVISIBLE
        datas.nia.visibility = View.INVISIBLE
        datas.nif.visibility = View.INVISIBLE
        datas.numero.visibility = View.INVISIBLE
        datas.textoNumero.visibility = View.INVISIBLE
        datas.buscar.visibility = View.INVISIBLE
        val usuario = intent.getStringExtra("username")
        findViewById<TextView>(R.id.usuarioFirebase).text = usuario

        val avatarByteArray = intent.getByteArrayExtra("avatar")

        if (avatarByteArray != null){
            val bitmap = BitmapFactory.decodeByteArray(avatarByteArray,0,avatarByteArray.size)
            findViewById<ImageView>(R.id.avatarFirebase).setImageBitmap(bitmap)
        }else{
            Log.d("Firebase","Avatar byte array is null")
        }
        datas.notaSuperior.setOnClickListener {
            datas.listado.visibility = View.VISIBLE
            alumnosRef.get().addOnSuccessListener { result ->
                val linearLayout = datas.listado.findViewById<LinearLayout>(R.id.listadoLinear)
                linearLayout.removeAllViews()
                for (document in result) {
                    val alumno = document.toObject(Alumno::class.java)
                    if (alumno.mediaexpediente>= 5 ){
                        val textView = TextView(this).apply {
                            text = "${alumno.nombre} ${alumno.apellido} - Media: ${alumno.mediaexpediente}"
                        }

                        linearLayout.addView(textView)
                    }
                }
            }.addOnFailureListener { error ->
                Log.d("ErrorFirebase", "Error obteniendo los documentos", error)
            }

        }
        datas.listarAlumnos.setOnClickListener {
            datas.listado.visibility = View.VISIBLE
            alumnosRef.get().addOnSuccessListener { result ->
                val linearLayout = datas.listado.findViewById<LinearLayout>(R.id.listadoLinear)
                linearLayout.removeAllViews()
                for (document in result) {
                    val alumno = document.toObject(Alumno::class.java)
                    val textView = TextView(this).apply {
                        text = "${alumno.nombre} ${alumno.apellido} - Media: ${alumno.mediaexpediente}"
                    }
                    linearLayout.addView(textView)
                }
            }.addOnFailureListener { error ->
                Log.d("ErrorFirebase", "Error obteniendo los documentos", error)
            }
        }
        datas.informacionAlumno.setOnClickListener {
            datas.nif.visibility = View.VISIBLE
            datas.nia.visibility = View.VISIBLE
            datas.numero.visibility = View.INVISIBLE // Se ocultará hasta que se seleccione una opción
            datas.textoNumero.visibility = View.INVISIBLE // Se ocultará hasta que se seleccione una opción
            datas.buscar.visibility = View.INVISIBLE // Se ocultará hasta que se seleccione una opción
        }
        datas.nia.setOnClickListener {
            datas.numero.visibility = View.VISIBLE
            datas.textoNumero.visibility = View.VISIBLE
            datas.buscar.visibility = View.VISIBLE
        }
        datas.nif.setOnClickListener {
            datas.numero.visibility = View.VISIBLE
            datas.textoNumero.visibility = View.VISIBLE
            datas.buscar.visibility = View.VISIBLE
        }
        datas.buscar.setOnClickListener {
            val numeroIngresado = datas.numero.text.toString().toIntOrNull()
            if (numeroIngresado == null) {
                Toast.makeText(this, "Por favor, ingresa un número válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val campoBusqueda = if (datas.nia.isChecked) "nia" else "nif"
            alumnosRef.whereEqualTo(campoBusqueda, numeroIngresado.toString()).get().addOnSuccessListener { resultado ->
                val linearLayout = datas.listado.findViewById<LinearLayout>(R.id.listadoLinear)
                linearLayout.removeAllViews()
                for (documento in resultado) {
                    val alumno = documento.toObject(Alumno::class.java)
                    val texto = TextView(this).apply {
                        text = "${alumno.nombre} ${alumno.apellido} - Media: ${alumno.mediaexpediente} - NIA: ${alumno.nia} - NIF: ${alumno.nif}"
                    }
                    linearLayout.addView(texto)
                }
                datas.listado.visibility = View.VISIBLE
            }.addOnFailureListener { error ->
                Log.d("ErrorFirebase", "Error obteniendo los documentos", error)
            }
        }
    }
}