package com.example.activitystates

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this,"CREANDO ACTIVIDAD",Toast.LENGTH_LONG).show()
        //Log.d("CREATE", "CREANDO LA ACTIVIDAD")
    }
    override fun onStart() {
        super.onStart()
        //Log.d("START", "ARRANCANDO LA ACTIVIDAD")
        Toast.makeText(this,"ARRANCANDO ACTIVIDAD",Toast.LENGTH_LONG).show()
        // La actividad est� a punto de hacerse visible.
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this,"REANUDANDO ACTIVIDAD",Toast.LENGTH_LONG).show()
        //Log.d("RESUME", "REANUDANDO LA ACTIVIDAD")
        // La actividad se ha vuelto visible (ahora se "reanuda").
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this,"PAUSANDO ACTIVIDAD",Toast.LENGTH_LONG).show()
        //Log.d("PAUSA", "PAUSANDO LA ACTIVIDAD")
        // Enfocarse en otra actividad  (esta actividad est� a punto de ser "detenida").
    }

    override  fun onStop() {
        super.onStop()
        Toast.makeText(this,"PARANDO ACTIVIDAD",Toast.LENGTH_LONG).show()
        //Log.d("STOP", "PARANDO LA ACTIVIDAD")
        // La actividad ya no es visible (ahora est� "detenida")
    }

    override  fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"DESTRUYENDO ACTIVIDAD",Toast.LENGTH_LONG).show()
        //Log.d("DESTROY", "DESTRUYENDO LA ACTIVIDAD")
        // La actividad est� a punto de ser destruida.
    }
}