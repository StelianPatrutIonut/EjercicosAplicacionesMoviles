package com.example.appdefinitiva10

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.Dataset
import android.view.View
import com.example.appdefinitiva10.databinding.ActivityMiniJuegoBinding
class miniJuego : AppCompatActivity() {
    private lateinit var datas : ActivityMiniJuegoBinding
    private lateinit var gameView: GameView
    private var usuario: String? = null
    private var avatarByteArray: ByteArray? = null
    private lateinit var musica : MediaPlayer
    private lateinit var userDao: UserDao
    private var estaPausado = false // Inicializa en falso para que comience reproduciendo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datas = ActivityMiniJuegoBinding.inflate(layoutInflater)
        setContentView(datas.root)
        val database = DatabaseManager.getDatabase(this)
        userDao = database.userDao()
        usuario = intent.getStringExtra("username")
        avatarByteArray = intent.getByteArrayExtra("avatar")
        datas.nombre.text = usuario
        avatarByteArray?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            datas.avatar3.setImageBitmap(bitmap)
        }
        musica = MediaPlayer.create(this, R.raw.retro)
        musica.isLooping = true

        // Crea la instancia de GameView y configura el puntaje
        gameView = GameView(this, usuario ?: "").apply {
            setScoreTextView(datas.scoreText)
            // No se llama a pause(), ya que queremos iniciar en reproducción
        }

        // Agrega la vista del juego al layout
        datas.juego.addView(gameView)

        // Configura el botón para iniciar el juego y la música cuando se presiona
        if (estaPausado) {
            // Si el juego está en pausa, configura el botón para iniciar
            datas.playPause.text = "PLAY"
        } else {
            // Si el juego está en reproducción, configura el botón para pausar
            datas.playPause.text = "PAUSE"
        }
        datas.playPause.setOnClickListener {
            if (estaPausado) {
                // Al presionar PLAY, inicia el juego y la música
                datas.playPause.text = "PAUSE"
                gameView.resume()
                if (!musica.isPlaying) {
                    musica.start()
                }
            } else {
                // Al presionar PAUSE, pausa el juego y la música
                datas.playPause.text = "PLAY"
                gameView.pause()
                musica.pause()
            }
            // Cambia el estado de estaPausado después de las condiciones
            estaPausado = !estaPausado
        }

        // Configura el botón para finalizar la partida
        datas.finPartida.setOnClickListener {
            finJuego(it)
        }

        // Inicia el hilo del juego y la música directamente
        gameView.resume()
        musica.start()
    }

    fun finJuego(view: View){
        gameView.endGame(usuario, avatarByteArray)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (musica.isPlaying) {
            musica.stop()
        }
        musica.release()
    }

    override fun onResume() {
        super.onResume()
        if (!estaPausado) {
            gameView.resume()
            musica.start()
        }
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }
}