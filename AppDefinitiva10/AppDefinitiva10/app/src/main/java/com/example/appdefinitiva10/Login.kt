package com.example.appdefinitiva10

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.appdefinitiva10.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class Login : AppCompatActivity() {
    private lateinit var data: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(data.root)
        data.login.setOnClickListener {
            val usuario = data.usuarioLogin.text.toString().trim()
            val contrasenia = data.password.text.toString().trim()
            val contraseniaEncriptada = encriptarContrasenia(contrasenia)

            Log.d("Login", "Intentando iniciar sesión con usuario: $usuario")

            lifecycleScope.launch(Dispatchers.IO) {
                val user = DatabaseManager.getDatabase(this@Login).userDao().encontrarUsuario(usuario, contraseniaEncriptada)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        Log.d("Login", "Inicio de sesión exitoso para el usuario: ${user.userName}")
                        val intent = Intent(this@Login, PantallaFinal::class.java).apply {
                            putExtra("username", user.userName)
                            putExtra("avatar",user.avatar)
                            putExtra("contrasenia",contraseniaEncriptada)
                        }
                        startActivity(intent)
                    } else {
                        Log.d("Login", "Credenciales de inicio de sesión incorrectas")
                        Toast.makeText(applicationContext, "El usuario o la contraseña no son correctas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun encriptarContrasenia(password: String): String{
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digested = md.digest(bytes)
        return digested.joinToString (""){ "%02x".format(it)}
    }
}