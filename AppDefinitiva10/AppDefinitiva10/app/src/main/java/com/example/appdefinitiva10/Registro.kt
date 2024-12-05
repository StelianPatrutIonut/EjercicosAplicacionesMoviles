package com.example.appdefinitiva10

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide

import com.example.appdefinitiva10.databinding.ActivityRegistroBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class Registro : AppCompatActivity() {

    private lateinit var datas : ActivityRegistroBinding
    private var byteArrayAvatar: ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datas = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(datas.root)
        datas.condiciones.isChecked=false
        datas.imagenRandom1.visibility = View.INVISIBLE
        datas.imagenRandom2.visibility = View.INVISIBLE
        datas.imagenRandom3.visibility = View.INVISIBLE
        datas.foto.visibility = View.INVISIBLE


        datas.fechaNacimiento.setOnClickListener{
            val calendario  = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                calendario.set(anioSeleccionado, mesSeleccionado, diaSeleccionado)
                datas.fechaNacimiento.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendario.time))
            }, anio, mes, dia).show()
        }
        datas.nuevoAvateres.visibility = View.INVISIBLE

        datas.avatar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                datas.imagenRandom1.visibility = View.VISIBLE
                datas.imagenRandom2.visibility = View.VISIBLE
                datas.imagenRandom3.visibility = View.VISIBLE
                datas.foto.visibility = View.INVISIBLE
                lifecycleScope.launch {
                    cargarImagenes()
                    datas.nuevoAvateres.visibility = View.VISIBLE
                    datas.radioButton3.isChecked=false
                    configurarListenersAvatar()
                    datas.nuevoAvateres.setOnClickListener {
                        lifecycleScope.launch {
                            cargarImagenes()
                           configurarListenersAvatar()
                        }


                    }
                }
            }
        }
        datas.radioButton3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                datas.imagenRandom1.visibility = View.INVISIBLE
                datas.imagenRandom2.visibility = View.INVISIBLE
                datas.imagenRandom3.visibility = View.INVISIBLE
                datas.foto.visibility = View.VISIBLE

                datas.avatar.isChecked= false

                tomarFoto.launch(null)
            }
        }
        datas.condiciones.setOnClickListener{
            mostrarCondiciones()
        }


        datas.Registrarse.setOnClickListener {
            val userName = datas.usuario.text.toString()
            val password = datas.contrasenia.text.toString()
            val confirmPassword = datas.contrasenia2.text.toString()
            val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaNacimiento = formatoFecha.parse(datas.fechaNacimiento.text.toString())
            val fechaActual = Calendar.getInstance().time
            if (!nombreUsuarioValido(userName)) {
                mostrarTexto("El usuario tiene que contener de 4 a 15 caracteres")
                return@setOnClickListener
            }

            if (!contraseniaValida(password)) {
                mostrarTexto("La contraseña no es válida")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                mostrarTexto("Las contraseñas no coinciden")
                return@setOnClickListener
            }

            if (fechaNacimiento == null || !fechaNacimiento.before(fechaActual)) {
                mostrarTexto("Fecha no válida")
                return@setOnClickListener
            }

            if (!datas.condiciones.isChecked) {
                mostrarTexto("Debes aceptar los términos y condiciones")
                return@setOnClickListener
            }

            if (byteArrayAvatar == null) {
                mostrarTexto("Debe seleccionar un avatar")
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val userExists = DatabaseManager.getDatabase(this@Registro).userDao().findByName(userName) != null

                withContext(Dispatchers.Main) {
                    if (userExists) {
                        mostrarTexto("El usuario ya existe en la base de datos")
                    } else {
                        registrarUsuario(userName, password, fechaNacimiento, byteArrayAvatar!!)
                    }
                }
            }


        }
    }

    private fun configurarListenersAvatar() {
        listOf(datas.imagenRandom1, datas.imagenRandom2, datas.imagenRandom3).forEach { imageButton ->
            imageButton.setOnClickListener { seleccionarAvatar(it as ImageButton) }
        }
    }
    private fun registrarUsuario(userName: String, password: String, fechaNacimiento: Date, byteArrayAvatar: ByteArray) {
        val contraseniaEncriptada = encriptarContrasenia(password)
        val user = User(
            id = 0,
            userName = userName,
            password = contraseniaEncriptada,
            avatar = byteArrayAvatar,
            fechaNacimiento = fechaNacimiento
        )

        lifecycleScope.launch(Dispatchers.IO) {
            DatabaseManager.getDatabase(this@Registro).userDao().insertAll(user)
            withContext(Dispatchers.Main) {
                mostrarTexto("Usuario registrado con éxito")
                volverAPantallaPrincipal()
            }
        }
    }
    private fun seleccionarAvatar(imageButton: ImageButton) {
        (imageButton.drawable as? BitmapDrawable)?.bitmap?.let { bitmap ->
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            byteArrayAvatar = stream.toByteArray()
        }
    }
    private val tomarFoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            datas.foto.setImageBitmap(bitmap)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            byteArrayAvatar = stream.toByteArray()
        } else {
            mostrarTexto("Ha habido un error a la hora de hacer foto")
        }
    }

    private fun mostrarCondiciones() {
        val dialogo = AlertDialog.Builder(this)
        dialogo.setTitle("Terminos y Condiciones")
        dialogo.setMessage("Al aceptar los termines y condiciones, " +
                "autorizas a la aplicacion y al creador de la aplicacion" +
                "a comercializar con tus datos libremente para lo que el quiere")
        dialogo.setPositiveButton("Aceptar"){_,_ ->
            datas.condiciones.isChecked=true
        }
        dialogo.setNegativeButton("Cancelar"){dialog,_ ->
            datas.condiciones.isChecked=false
            dialog.dismiss()

        }
        dialogo.create().show()
    }

    private fun nombreUsuarioValido(nombre: String): Boolean{
        return nombre.length in 4..15
    }
    private fun contraseniaValida(contrasenia: String): Boolean{
        val patronContrasenia = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[,._@#$%&\\-]).{6,15}$".toRegex()
        return contrasenia.matches(patronContrasenia)
    }
    private fun mostrarTexto(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun encriptarContrasenia(password: String): String{
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digested = md.digest(bytes)
        return digested.joinToString (""){ "%02x".format(it)}
    }


    private fun volverAPantallaPrincipal() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    private suspend fun cargarImagenes() {

        try {
            val urlImage = obtenerUrlImagen()
            cargarImagenDesdeUrl(urlImage, datas.imagenRandom1)
            val urlImage2 = obtenerUrlImagen()
            cargarImagenDesdeUrl(urlImage2, datas.imagenRandom2)
            val urlImage3 = obtenerUrlImagen()
            cargarImagenDesdeUrl(urlImage3, datas.imagenRandom3)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private suspend fun obtenerUrlImagen(): String {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RandomUserGeneratorApi::class.java)
        val respuesta= service.getRandomUser()
        return respuesta.results.first().picture.medium



    }

    private fun cargarImagenDesdeUrl(imageUrl: String?, imagenRandom: ImageView) {

        Glide.with(this)
            .load(imageUrl)
            .into(imagenRandom)

    }

}