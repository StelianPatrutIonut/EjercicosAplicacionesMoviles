package com.example.appdefinitiva10

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.appdefinitiva10.databinding.ActivityPantallaFinalBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
import java.security.MessageDigest


class PantallaFinal : AppCompatActivity() {
    private lateinit var datas: ActivityPantallaFinalBinding
    private val REQ_GOOGLE_SIGN_IN = 123 // Código de solicitud para iniciar sesión con Google
    private lateinit var auth: FirebaseAuth // Instancia de FirebaseAuth


    // Variables para mantener el nombre de usuario y el avatar
    private var usuario: String? = null
    private var usuario1 : String =""
    private var avatarByteArray: ByteArray? = null
    private var contraseniaEncriptada : String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datas = ActivityPantallaFinalBinding.inflate(layoutInflater)
        setContentView(datas.root)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Recuperar datos pasados a esta actividad
        usuario = intent.getStringExtra("username")

        avatarByteArray = intent.getByteArrayExtra("avatar")
        contraseniaEncriptada = intent.getStringExtra("contrasenia").toString()

        // Configurar los elementos de la UI
        datas.usuarioFinal.text = usuario
        avatarByteArray?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            datas.avatarFoto.setImageBitmap(bitmap)
        } ?: Log.d("PantallaFinal", "Avatar byte array is null")

        // Listener para el botón que inicia la actividad Firebase
        datas.firebase.setOnClickListener {
            autenticarUsuarioAnonimo()
        }

        // Listener para el botón de autenticación de Google
        datas.firebase2.setOnClickListener {
            signOutGoogle() // Opcional: cerrar sesión de Google antes de iniciar otra sesión
            iniciarSesionConGoogle()
        }
        datas.minijuego.setOnClickListener {
            val intent = Intent(this, miniJuego::class.java).apply {
                putExtra("username", usuario)
                putExtra("avatar", avatarByteArray)
            }

            startActivity(intent)
        }
        // Iniciar una corutina en el contexto de la UI
        usuario?.let {
            actualizarUI(it)
        }
        datas.volverPantalla.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)

            startActivity(intent)
        }
        datas.cambiarContrasenia.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.modificar_contrasena2, null)
            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Modificar Contraseña")
                .setPositiveButton("Modificar") { _, _ ->
                    val username = usuario
                    val oldPassword = contraseniaEncriptada
                    val newPassword = dialogView.findViewById<EditText>(R.id.editTextNewPassword).text.toString()

                    username?.let {
                        actualizarContrasena(it, oldPassword, newPassword)
                    }
                }
                .setNegativeButton("Cancelar", null)
                .create()

            alertDialog.show()
        }

    }
    private fun actualizarContrasena(username: String, oldPassword: String, newPassword: String) {
        lifecycleScope.launch {
            val userExists = withContext(Dispatchers.IO) {
                DatabaseManager.getDatabase(applicationContext).userDao().findUserByUsernameAndPassword(username, oldPassword) != null
            }

            if (userExists) {
                val contraseniaEncriptada = encriptarContrasenia(newPassword)
                withContext(Dispatchers.IO) {
                    DatabaseManager.getDatabase(applicationContext).userDao().updatePassword(username, contraseniaEncriptada)
                }
            }
        }
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

    private fun actualizarUI(usuario: String) {
        lifecycleScope.launch {
            val juegoResultado = withContext(Dispatchers.IO) {
                // Asegúrate de que esta operación se ejecute en un hilo de fondo
                val db = DatabaseManager.getDatabase(applicationContext)
                db.juegoResultadoDao().getJuegoResultado(usuario)
            }

            // Actualiza la UI en el hilo principal
            juegoResultado?.let {
                datas.maxScore.text = "Max Score: ${it.puntuacionMaxima}"
                datas.totalScore.text = "Total Score: ${it.puntuacionAcumulada}"
            }
        }
    }


    private fun autenticarUsuarioAnonimo() {
        auth.signInAnonymously()
            .addOnSuccessListener { authResult ->
                // Autenticación exitosa, ahora iniciar la actividad Firebase y pasar los datos
                val intent = Intent(this, Firebase::class.java).apply {
                    putExtra("username", usuario)
                    putExtra("avatar", avatarByteArray)
                }
                startActivity(intent)
            }
            .addOnFailureListener { exception ->

                Toast.makeText(baseContext, "Error de autenticación: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun iniciarSesionConGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id)) // ID de cliente de OAuth 2.0
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        startActivityForResult(googleSignInClient.signInIntent, REQ_GOOGLE_SIGN_IN)
    }

    private fun signOutGoogle() {
        val googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        googleSignInClient.signOut().addOnCompleteListener(this) {
            // Opcional: puedes realizar acciones adicionales aquí después de cerrar la sesión
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("PantallaFinal", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("PantallaFinal", "signInWithCredential:success")
                val user = auth.currentUser
                iniciarActividadFirebaseGoogle()
            } else {
                Log.w("PantallaFinal", "signInWithCredential:failure", task.exception)
            }
        }
    }

    private fun iniciarActividadFirebaseGoogle() {
        val intent = Intent(this, FirebaseGoogle::class.java).apply {
            putExtra("username", usuario)
            putExtra("avatar", avatarByteArray)
        }
        startActivity(intent)
    }

}
