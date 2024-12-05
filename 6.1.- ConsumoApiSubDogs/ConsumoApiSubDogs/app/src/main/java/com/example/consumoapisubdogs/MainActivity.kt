package com.example.consumoapisubdogs

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.consumoapisubdogs.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Timer
import java.util.TimerTask



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DogApiService::class.java)

        binding.loadImageButton.setOnClickListener {

            val call = service.getBreeds();
            call.enqueue(object : Callback<DogApiResponse> {
                override fun onResponse(call: Call<DogApiResponse>,response: Response<DogApiResponse>) {
                    if (response.isSuccessful) {
                        val razaSubRaza = response.body()?.message
                        if (!razaSubRaza.isNullOrEmpty()) {
                            val raza = binding.editTextText.text.toString();
                            var listSubRazas = razaSubRaza[raza]
                            // entro aquí si la raza que busco no tiene subrazas. Resuelvo como lo teníamos antes.
                            if (listSubRazas.isNullOrEmpty()) {
                                val call = service.getBreedRandomImage(raza)
                                showImageBreed(call, binding)
                            } else { //sino la raza tiene subrazas, por tanto, debo pintar en el Spinner y que el usuario pueda elegir una de ellas
                                //updateSpinner(listSubRazas, binding)
                                Log.d("SUBRAZAS", listSubRazas.toString())
                                binding.loadImageButton.visibility = View.INVISIBLE
                                binding.loadImageButton2.visibility = View.VISIBLE
                                binding.spinner.visibility = View.VISIBLE
                                updateSpinner(this@MainActivity , listSubRazas, binding)
                                binding.editTextText.setText("")
                                binding.editTextText.visibility = View.INVISIBLE
                                binding.loadImageButton2.setOnClickListener{
                                    Log.d("SUBRAZAS", "sgundo boton")
                                    //val subraza = binding.editTextText.text.toString()
                                    val subraza = binding.spinner.selectedItem.toString()
                                    val call = service.getSubBreedRandomImage(raza,subraza)
                                    showImageSubBreed(call,binding)

                                    Timer().schedule(object : TimerTask() {
                                        override fun run() {
                                            val intent = intent
                                            finish()
                                            startActivity(intent)
                                        }
                                    }, 15000)
                             }
                            }
                        }
                    }
                } //onResponse
                override fun onFailure(call: Call<DogApiResponse>, t: Throwable) {
                    // Manejar el fallo de la solicitud
                    Log.d("MARIO", "ERROR BUSCANDO RAZAS-SUBRAZAS")
                } //onFailure
            }) //call.enqueue
    } //binding.loadImage....
    } //onCreate


} //class
private fun showImageSubBreed (call: Call<DogSubRazaResponse>, binding: ActivityMainBinding ){
    call.enqueue(object : Callback<DogSubRazaResponse> {
        override fun onResponse(call: Call<DogSubRazaResponse>, response: Response<DogSubRazaResponse>) {
            if (response.isSuccessful) {
                Log.d("MARIO", "dENTRO DEL 1ER IF")
                val sizeSubRazas = response.body()?.message?.size;

                val imageUrl = response.body()?.message?.get((0..sizeSubRazas!!).random())
                if (!imageUrl.isNullOrEmpty()) {
                    // Utiliza Picasso u otra biblioteca para cargar la imagen en el ImageView
                    Log.d("MARIO", "dENTRO DEL SEGUNDO IF")
                    Picasso.get().load(imageUrl).into(binding.dogImageView)
                }
            }
        }

        override fun onFailure(call: Call<DogSubRazaResponse>, t: Throwable) {
            Log.d("MARIO", "ERROR BUSCANDO subRAZAS")
        }
    })
} //showImageSubBreed


private fun showImageBreed (call: Call<DogRazaResponse>, binding: ActivityMainBinding ){
                call.enqueue(object : Callback<DogRazaResponse> {
                    override fun onResponse(call: Call<DogRazaResponse>, response: Response<DogRazaResponse>) {
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
                    override fun onFailure(call: Call<DogRazaResponse>, t: Throwable) {
                        Log.d("MARIO", "ERROR BUSCANDO RAZAS")
                    }
                })
    } //showImageBreed

private fun updateSpinner(context: Context, breeds: List<String>, binding: ActivityMainBinding) {
    val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, breeds)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.spinner.adapter = adapter
}


