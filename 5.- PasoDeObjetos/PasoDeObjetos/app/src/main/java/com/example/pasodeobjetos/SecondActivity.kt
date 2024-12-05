package com.example.pasodeobjetos

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.pasodeobjetos.databinding.ActivitySecondBinding
import java.io.Serializable

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // PARA EL PRIMER PASO DE OBJETOS
        val v = intent.getSerializableExtra("car", Car::class.java) as Car?

        Log.d("MARIO_SERIALIZABLE", v?.name.toString())
        Log.d("MARIO_SERIALIZABLE ", v?.model.toString())


        /* PARA EL SEGUNDO PASO DE OBJETOS PARCELABLE */
        /*val coche = intent.getParcelableExtra("coche",Coche::class.java)
        Log.d("MARIO_PARCELABLE", coche?.marca.toString())
        Log.d("MARIO_PARCELABLE ", coche?.modelo.toString())
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
    //  https://stackoverflow.com/questions/73019160/the-getparcelableextra-method-is-deprecated
    //  https://stackoverflow.com/questions/72571804/getserializableextra-and-getparcelableextra-are-deprecated-what-is-the-alternat
    //  https://stackoverflow.com/questions/73388006/android-13-sdk-33-bundle-getserializablestring-is-deprecated-what-is-alter
/////////////////////////////////////////////////////////////////////////////////////////////////
}
    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

}


