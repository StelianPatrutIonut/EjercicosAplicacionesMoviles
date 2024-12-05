package com.example.arqmvc_v0.View

import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.arqmvc_v0.R
import android.content.Intent

class MyView(private val activity: AppCompatActivity) {

    private val textView: TextView = activity.findViewById(R.id.textView2)
    private val button1: Button = activity.findViewById(R.id.button)
    private val button2: Button = activity.findViewById(R.id.button2)
    private val button3: Button = activity.findViewById(R.id.button3)
    private val textView1: TextView = activity.findViewById(R.id.contador1)
    private val textView2: TextView = activity.findViewById(R.id.contador2)
    private val textView3: TextView = activity.findViewById(R.id.contador3)
    private val mostrar: TextView = activity.findViewById(R.id.usuarioRecogido)

    init {
        // Obtén el nombre de usuario del Intent
        val intent = activity.intent
        val usuarioRecogido = intent.getStringExtra("usuario")

        // Muestra el nombre de usuario en el TextView mostrar
        mostrar.text = "Usuario Recogido: $usuarioRecogido"
    }



    fun setCounterText(counter: Int,counter2: Int, counter3:Int, counter4: Int) {
        textView.text = "Contador: $counter"
        textView1.text = "Contador: $counter2"
        textView2.text = "Contador: $counter3"
        textView3.text= "Contador: $counter4"
    }

    /* Esta función establece un OnClickListener para el button1 y ejecuta la función listener
cuando se hace clic en el botón. El listener es proporcionado por la clase MyController y
contiene la lógica que se debe ejecutar cuando se hace clic en el botón.*/
    fun setButton1ClickListener(listener: () -> Unit) {
/*    Configura el OnClickListener para el button1 de manera que, cuando se hace clic en el botón,
se ejecute la función listener proporcionada.*/
        button1.setOnClickListener { listener() }
    }

    fun setButton2ClickListener(listener: () -> Unit) {
        button2.setOnClickListener { listener() }
    }

    fun setButton3ClickListener(listener: () -> Unit) {
        button3.setOnClickListener { listener() }

}

/* Estas funciones en están diseñadas para permitir que el controlador asigne funciones
personalizadas que se ejecutarán cuando los botones sean presionados.
Al usar lambdas (listener: () -> Unit), se permite una mayor flexibilidad en la lógica que
puede ejecutarse cuando se realiza una acción en la vista. En este caso, la lógica asociada
con cada botón se especifica en el Controller.
 */
}