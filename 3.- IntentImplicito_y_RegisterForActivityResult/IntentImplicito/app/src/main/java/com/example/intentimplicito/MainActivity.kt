package com.example.intentimplicito

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.intentimplicito.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {


        //USO DE LA DEPRECATED startActivityForResult() -- registerForActivityResult()
        var secondActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                //receive data from secondActivity
                if (data != null) {
                    // Here the data which we have sent from the second activity is received and displayed in the textview.
                    val name = data.getStringExtra(NAME)
                    val email = data.getStringExtra(EMAIL)
                    binding.userText.setText(name)
                    binding.mailText.setText(email)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Log.e("Cancelled", "Cancelled")
                Toast.makeText(this,"Result Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button1.setOnClickListener {

            val callIntent: Intent = Uri.parse("tel:5551234").let { number ->
                Intent(Intent.ACTION_DIAL, number)
            }
            startActivity(callIntent)
        }
        binding.button2.setOnClickListener{
            // Map point based on address
            val mapIntent: Intent = Uri.parse(
                "geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California"
            ).let { location ->
                // Or map point based on latitude/longitude
                // val location: Uri = Uri.parse("geo:37.422219,-122.08364?z=14") // z param is zoom level
                Intent(Intent.ACTION_VIEW, location)
            }
            startActivity(mapIntent)

        }
        binding.button3.setOnClickListener {
            val webIntent: Intent = Uri.parse("https://palestinalibre.org/").let { webpage ->
                Intent(Intent.ACTION_VIEW, webpage)
            }
            startActivity(webIntent)
        }
        binding.button4.setOnClickListener {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "hola")
                putExtra(AlarmClock.EXTRA_HOUR, 22)
                putExtra(AlarmClock.EXTRA_MINUTES, 0)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        binding.button5.setOnClickListener {

            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            //Ahora no lanzo un startActivity(intent), sino que lanzo la siguiente linea que envía y recibe
            secondActivityResultLauncher.launch(intent)

        }
    }
// https://www.develou.com/objetos-companeros-en-kotlin/
    companion object{
        const val NAME = "name"
        const val EMAIL = "email"
    }
}