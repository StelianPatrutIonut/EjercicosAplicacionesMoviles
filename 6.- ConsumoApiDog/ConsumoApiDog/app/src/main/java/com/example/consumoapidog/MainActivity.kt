package com.example.consumoapidog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.consumoapidog.databinding.ActivityMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
 

class MainActivity : AppCompatActivity() {

    private lateinit var dogImageView: ImageView
    private lateinit var loadImageButton: Button
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.loadImageButton.setOnClickListener {
            // Lógica para cargar una imagen aleatoria de la raza ingresada por el usuario
            // Utiliza Retrofit para realizar la solicitud a la API
          //  val breed = binding.editTextText.text
            //val breed = "affenpinscher" // Reemplaza con la raza ingresada por el usuario
            val breed = binding.editTextText.text.toString()
            Log.d("MARIO", breed.toString())


            val retrofit = Retrofit.Builder()
                .baseUrl("https://dog.ceo/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(DogApiService::class.java)
            val call = service.getRandomImage(breed)





            call.enqueue(object : Callback<DogApiResponse> {
                override fun onResponse(call: Call<DogApiResponse>, response: Response<DogApiResponse>) {
                    if (response.isSuccessful) {
                        Log.d("MARIO", "dENTRO DEL 1ER IF")
                        val imageUrl = response.body()?.message
                        if (!imageUrl.isNullOrEmpty()) {
                            // Utiliza Picasso u otra biblioteca para cargar la imagen en el ImageView
                            Log.d("MARIO", "dENTRO DEL SEGUNDO IF")
                            Picasso.get().load(imageUrl).into(binding.dogImageView)
                        }
                    }
                }

                override fun onFailure(call: Call<DogApiResponse>, t: Throwable) {
                    // Manejar el fallo de la solicitud
                    Log.d("MARIO", "dERRRRRRRRRRR")
                }
            })
        }
    }
}