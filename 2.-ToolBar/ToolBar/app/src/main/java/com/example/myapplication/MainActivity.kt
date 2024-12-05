package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            Toast.makeText(this,"HE PULSADO SETTINGS, ME VOY A OTRA ACTIVITY",Toast.LENGTH_LONG).show()// User chooses the "Settings" item. Show the app settings UI.
            val intent = Intent(this, SettingsActivity::class.java)
            //intent.putExtra("DATOS", datos.getText().toString())
            startActivity(intent)
            true
        }

        R.id.action_favorite -> {
            // User chooses the "Favorite" action. Mark the current item as a
            // favorite.
            Toast.makeText(this,"HE PULSADO FAVORITE, ME QUEDO POR AQUÍ",Toast.LENGTH_LONG).show()
            true
        }

        else -> {
            // The user's action isn't recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

}