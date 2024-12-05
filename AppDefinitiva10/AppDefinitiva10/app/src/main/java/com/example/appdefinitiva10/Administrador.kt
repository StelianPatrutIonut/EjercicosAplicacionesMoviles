package com.example.appdefinitiva10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.appdefinitiva10.databinding.ActivityAdministradorBinding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class Administrador : AppCompatActivity() {
    private lateinit var data: ActivityAdministradorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = ActivityAdministradorBinding.inflate(layoutInflater)
        setContentView(data.root)
          data.borrar.setOnClickListener {
              AlertDialog.Builder(this)
                  .setTitle("Confirmar eliminacion")
                  .setMessage("¿Estás seguro de que quieres eliminar todos los usuarios?")
                  .setPositiveButton("Si") {dialog, which ->
                      GlobalScope.launch (Dispatchers.IO){
                          DatabaseManager.getDatabase(applicationContext).userDao().deleteAllUsers()
                          DatabaseManager.getDatabase(applicationContext).juegoResultadoDao().deleteAllUsers()
                      }
                      mostrarTexto("Todos los usuarios han sido eliminados")
                  }
                  .setNegativeButton("No", null)
                  .show()
          }
        /*
         data.modificacion.setOnClickListener {
              val dialogView = LayoutInflater.from(this).inflate(R.layout.modificar_contrasena, null)
              val alertDialog = AlertDialog.Builder(this)
                  .setView(dialogView)
                  .setTitle("Modificar Contraseña")
                  .setPositiveButton("Modificar", null)
                  .setNegativeButton("Cancelar", null)
                  .create()

              alertDialog.show()


              alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                  val username = dialogView.findViewById<EditText>(R.id.editTextUsername).text.toString()
                  val oldPassword = encriptarContrasenia(dialogView.findViewById<EditText>(R.id.editTextOldPassword).text.toString())
                  val newPassword = dialogView.findViewById<EditText>(R.id.editTextNewPassword).text.toString()

                  if (!contraseniaValida(newPassword)) {
                      mostrarTexto("La contraseña no cumple con los requisitos.")
                      return@setOnClickListener
                  }

                  lifecycleScope.launch {
                      val userExists = withContext(Dispatchers.IO) {
                          DatabaseManager.getDatabase(applicationContext).userDao().findUserByUsernameAndPassword(username, oldPassword) != null
                      }

                      if (userExists !=null) {
                          val contraseniaEncriptada = encriptarContrasenia(newPassword)
                          withContext(Dispatchers.IO) {
                              DatabaseManager.getDatabase(applicationContext).userDao().updatePassword(username, contraseniaEncriptada)
                          }
                          mostrarTexto("Contraseña actualizada con éxito.")
                          alertDialog.dismiss()
                      } else {

                          mostrarTexto("Error: Las credenciales no son válidas.")
                      }
                  }
          }
          }*/
        data.volver.setOnClickListener {
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        data.listado.setOnClickListener {
            var intent = Intent(this,Listado::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarTexto(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun contraseniaValida(contrasenia: String): Boolean {
        val patronContrasenia = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[,._@#$%&\\-]).{6,15}$".toRegex()
        return contrasenia.matches(patronContrasenia)
    }
    private fun encriptarContrasenia(password: String): String{
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digested = md.digest(bytes)
        return digested.joinToString (""){ "%02x".format(it)}
    }


    }
