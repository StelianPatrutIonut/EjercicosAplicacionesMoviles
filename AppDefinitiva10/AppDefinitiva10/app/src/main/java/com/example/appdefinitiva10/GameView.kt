package com.example.appdefinitiva10

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.doOnAttach

class GameView(context: Context, private val username: String) : SurfaceView(context), SurfaceHolder.Callback, Runnable, View.OnTouchListener  {
    //Hilo del juego
    private lateinit var gameThread: Thread
    //Booleano con el estado del juego (jugando o no)
    private var playing = false
    //Caracterización de la bola
    private val ballPaint = Paint()
    private val ballMainPaint = Paint()
    //Caracterización del Square
    private val squarePaint = Paint()
    //Objeto Bola
    private var ball1: Ball? = null
    private var ball2: Ball? = null

    private val balls: MutableList<Ball> = mutableListOf()

    //Tamaño de cada Square
    private val squareSize = 20f
    //Conjunto de Squares
    private var squares: Array<Array<String>>? = null
    private  var collision : MediaPlayer
    private  var muroCollision : MediaPlayer
    private var score: Int = 0
    private var scoreTextView: TextView? = null
    fun setScoreTextView(textView: TextView) {
        scoreTextView = textView
    }


    /* init{}
       ES UNA ESPECIE DE CONSTRUCTOR, PERO CON OTROS MATICES.
       https://stackoverflow.com/questions/55356837/what-is-the-difference-between-init-block-and-constructor-in-kotlin
       PODREMOS INICIALIZAR LA BOLA Y EL “SQUARE” QUE HABREMOS DEFINIDO COMO OBJETOS DE LA CLASE PAINT() USANDO LA CLASE COLOR()
        */
    init {
        holder.addCallback(this)
        //Asignar color a ball y square
        ballPaint.color = Color.BLACK
        //ballMainPaint.color = Color.RED
        squarePaint.color = Color.WHITE
        collision = MediaPlayer.create(context,R.raw.pop)
        muroCollision = MediaPlayer.create(context,R.raw.error)
        setOnTouchListener(this)
    }
    //https://developer.android.com/reference/kotlin/android/view/View.OnTouchListener

    // Método onTouch para manejar los eventos de toque
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN){
            val touchX = event.x
            val touchY = event.y
            ball1?.let{ball1 ->
                val angle = Math.atan2((touchY - ball1.y).toDouble(), (touchX - ball1.x).toDouble())
                ball1.dx = (5 * Math.cos(angle)).toFloat()
                ball1.dy = (5 * Math.sin(angle)).toFloat()
            }
        }
        return true
    }

    private fun checkCollisionWithList(ball: Ball, balls: MutableList<Ball>) {
        val iterator = balls.iterator()
        while (iterator.hasNext()) {
            val otherBall = iterator.next()
            if (ball !== otherBall && isCollision(ball, otherBall)) {
                collision.start()

                updateScore(otherBall.radius.toInt())
                iterator.remove()
            }
        }
        if(balls.isEmpty()){

            initializeBalls2(incremento = true)
            if (score - 500 > 0){
                score -= 500
                (context as Activity).runOnUiThread {
                    scoreTextView?.text = "Score: $score"
                }

            }else{
                score = 0
                (context as Activity).runOnUiThread {
                    scoreTextView?.text = "Score: $score"
                }
            }

        }
    }
    private fun updateScore2(newScore: Int) {
        var Score = score
        val scoreCambiado = Score - 500
        if (scoreCambiado < 0 ){
            score==0
        }else{

            score -=500
            (context as Activity).runOnUiThread {
                scoreTextView?.text = "Score: $score"
            }
        }
    }

    private fun initializeBalls2(incremento: Boolean = false){
        balls.clear()
        val random = java.util.Random()
        val numeroBolas = random.nextInt(4) + 3  // Número aleatorio de bolas
        for (i in 1..numeroBolas) {
            val x = random.nextInt(width - 100) + 50f
            val y = random.nextInt(height - 100) + 50f
            var dx = random.nextInt(11) - 5f
            var dy = random.nextInt(11) - 5f
            if (incremento){
                dx *= 4f
                dy *= 4f
            }
            var color: Int
            do {
                color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
            } while (color == Color.RED)
            val size = random.nextInt(41) + 20f
            balls.add(Ball(x, y, dx, dy, color, size))
        }
        var color2  = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        ball1 = Ball(width / 4f, height / 2f, 5f, 5f,color2 , squareSize / 2 + 10)





        /*
        balls.add(Ball(x = width / 2f, y = height / 4f, dx = -4f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = 3f, dy = -5f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = 1f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = -2f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = 3f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = -1f, dy = -6f))*/
        // Agregar más bolas si es necesario
    }

    fun isCollision(ball1: Ball, ball2: Ball): Boolean {
        val distanceX = ball1.x - ball2.x
        val distanceY = ball1.y - ball2.y
        val distance = Math.sqrt((distanceX * distanceX + distanceY * distanceY).toDouble())

        return distance < (ball1.radius + ball2.radius)
    }
    private fun updateScore(newScore: Int) {
        score += newScore

        (context as Activity).runOnUiThread {
            scoreTextView?.text = "Score: $score"
        }
    }

    fun guardarPuntuacion() {
        val newScore = this.score // O como obtengas la puntuación actual

        Thread {
            // Referencia al DAO de juegoResultado
            val juegoResultadoDao = DatabaseManager.getDatabase(context).juegoResultadoDao()

            // Intenta obtener el resultado del juego de la base de datos
            val juegoResultadoActual = juegoResultadoDao.getJuegoResultado(username)

            if (juegoResultadoActual != null) {
                // Si el resultado del juego existe, actualiza su puntuación acumulada y máxima puntuación
                juegoResultadoActual.puntuacionAcumulada += newScore // Asegúrate de que juegoResultado tenga una propiedad puntuacionAcumulada
                juegoResultadoActual.puntuacionMaxima = maxOf(juegoResultadoActual.puntuacionMaxima, newScore) // Asegúrate de que juegoResultado tenga una propiedad puntuacionMaxima
                juegoResultadoDao.update(juegoResultadoActual)
            } else {
                // Si no existe, crea un nuevo registro con la puntuación actual como puntuación inicial y máxima
                val nuevoJuegoResultado = juegoResultado(nombre = username, puntuacionAcumulada = newScore, puntuacionMaxima = newScore)
                juegoResultadoDao.insert(nuevoJuegoResultado)
            }
        }.start()
    }



    /* run()
    SERÁ UN MÉTODO QUE SE EJECUTARÁ DE FORMA “INFINITA” HASTA QUE OCURRA ALGO EN EL JUEGO
    -->> ¿QUE PUEDE OCURRIR EN UNA INTERFAZ COMO LA QUE HABÉIS VISTO?. <<--
    PODREMOS DEFINIR ALGÚN TIPO DE VARIABLE BOOLEANA (PLAYING POR EJEMPLO).
    LLAMAREMOS DE FORMA ININTERRUMPIDA A LOS MÉTODOS UPDATE(), DRAW() Y CONTROL().
    */
    override fun run(){
        while (playing){
            update()
            draw()
            control()
        }
    }
    /* update()
   ACTUALIZARÁ LA POSICIÓN DE CADA UNA DE LAS BOLAS DE LA PANTALLA.
   ESPECIAL ATENCIÓN A LAS VARIABLES "FANTASMA" width y height.
   -->> ¿QUE MÁS COSAS PODRÍA ACTUALIZAR ESTA FUNCION UPDATE?
    */
    private fun update(){
        ball1?.updatePosition(true)
        balls.forEach{it.updatePosition(false)}
        ball1?.let { checkCollisionWithList(it, balls) }
    }

    /* draw()
    EN CASO DE QUE EL "HOLDER SURFACE" (SOPORTE DE SUPERFICIE) CONSTRUIDO SEA CORRECTO,
    PINTA LOS SQUARES Y LA BOLA MEDIANTE CANVAS.
    BLOQUEO Y DESBLOQUEO MIENTRAS SE PINTA.
     */
    private fun draw() {
        val canvas = holder.lockCanvas()
        if (canvas != null) {
            try {
                drawSquares(canvas)
                drawScore(canvas) // Dibujar el puntaje

                // Crea una copia de la lista antes de iterar para evitar la modificación concurrente
                val ballsCopy = ArrayList(balls)
                ballsCopy.forEach { drawBall(canvas, it) }

                ball1?.let { drawBall1(canvas, it) }
            } finally {
                // Asegúrate de que el canvas siempre se desbloquea y se publica, incluso si ocurre una excepción
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    private fun drawScore(canvas: Canvas){
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 50f

    }
    /* control()
    ¿QUE OS PUEDO CONTAR YO DE THREADS QUE NO OS HAYA CONTADO DIEGO? :-)
    */
    private fun control(){
        try {
            Thread.sleep(17)
        }catch (e: InterruptedException){
            e.printStackTrace()
        }
    }
    /* drawBall()
      PINTAMOS UN CIRCULO CON LAS CARACTERÍSTICAS DE LA BOLA (QUIEN PINTA ES LA CLASE CANVA)
  */
    private fun drawBall(canvas: Canvas, ball: Ball) {
        val paint = if (ball == ball1) ballMainPaint else Paint().apply { color = ball.color }
        canvas.drawCircle(ball.x, ball.y, ball.radius, paint)
    }
    private fun drawBall1(canvas: Canvas, ball:Ball){
        ballPaint.color = ball.color
        canvas.drawCircle(ball.x,ball.y,ball.radius,ballPaint)
    }
    /* drawSquares()
    PINTAMOS RECTÁNGULOS ¿EN UNA MATRIZ?
    ESTAMOS RELLENANDO EL "CONJUNTO DE SQUARES" QUE DEFINIMOS AL PRINCIPIO
    */
    private fun drawSquares(canvas: Canvas){
        squares?.let { squaresArray ->
            for (i in squaresArray.indices){
                for (j in squaresArray[i].indices){
                    canvas.drawRect(
                        i * squareSize,
                        j * squareSize,
                        (i + 1) * squareSize,
                        (j + 1) * squareSize,
                        squarePaint
                    )
                }
            }
        }
    }

    /* pause()
       EN CASO DE PAUSAR EL JUEGO POR ALGUNA RAZÓN EXTERNA, YA QUE NO HAY BOTON DE PAUSA (DE MOMENTO)
     */
    fun pause() {
        playing = false
        if (::gameThread.isInitialized) {
            try {
                gameThread.join()
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }
    fun endGame(usuario: String?, avatarByteArray: ByteArray?) {
        guardarPuntuacion()  // Guarda la puntuación en la base de datos
        playing = false  // Detiene el juego
        pause()

        val intent = Intent(context, PantallaFinal::class.java).apply {
            putExtra("username", usuario)
            putExtra("avatar", avatarByteArray)
        }
        context.startActivity(intent)
        (context as Activity).finish()
    }
    /* resume()
        EN CASO DE REANUDAR EL JUEGO, TRAS ALGÚN SUCESO O EVENTO EXTERNO.
    */
    fun resume(){
        playing = true
        gameThread = Thread(this)
        gameThread.start()
    }
    /* DEFINIERMOS UNA INNER CLASS:
     https://wiki.yowu.dev/es/Knowledge-base/Kotlin/Learning/043-nested-and-inner-classes-in-kotlin-creating-classes-within-classes
    ¿CUALES SON LOS ATRIBUTOS DE LA INNER CLASS?

    ESTA CLASE TIENE UN ÚNICO MÉTODO QUE ACTUALIZA LA POSICIÓN DE LA BOLA Y COMPRUEBA SI HAY COLISIÓN.
    */

    inner class Ball(var x:Float, var y:Float, var dx: Float = 5f, var dy:Float = 5f,var color: Int, var radius: Float){

        fun updatePosition(redBall: Boolean){
            x += dx
            y += dy
            checkCollision(this,redBall)
        }

    }
    /* checkCollision(Ball)
     EN CASO DE COLISIÓN, DEBEMOS CAMBIAR EL SENTIDO DE LA BOLA
     TENEMOS QUE TENER CLARO QUE LA BOLA PUEDE REBOTAR ARRIBA, ABAJO, IZQUIERDA Y DERECHA,
     ES DECIR, EJE X E Y.
     ADEMÁS LA BOLA AL REBOTAR, NO DEBERÍA DEJAR DE VERSE.
  */
    private fun checkCollision(ball: Ball, redBall: Boolean){
        if (ball.x - ball.radius < 0 || ball.x + ball.radius > width || ball.y - ball.radius < 0 || ball.y + ball.radius > height){
            if (redBall) {
               muroCollision.start() // Reproduce el sonido de colisión con la pared
            }
            if (ball.x - ball.radius < 0 || ball.x + ball.radius > width){
                ball.dx = -ball.dx
            }
            if (ball.y - ball.radius < 0 || ball.y + ball.radius > height){
                ball.dy = -ball.dy
            }
        }
    }

    /* surfaceCreated(holder: SurfaceHolder)
      SE UBICA-POSICIONA EL OBJETO BOLA Y SE INICIALIZAN LOS "SQUARES"
    */
    override fun surfaceCreated(holder: SurfaceHolder) {
        ball1 = Ball(width / 4f, height / 2f, 5f, 5f, Color.RED, squareSize / 2 + 10)
        initializeBalls()
        initializeSquares(width, height)
        resume()
    }
    private fun initializeBalls(incremento: Boolean = false){
        balls.clear()
        val random = java.util.Random()
        val numeroBolas = random.nextInt(4) + 3  // Número aleatorio de bolas
        for (i in 1..numeroBolas) {
            val x = random.nextInt(width - 100) + 50f
            val y = random.nextInt(height - 100) + 50f
            var dx = random.nextInt(11) - 5f
            var dy = random.nextInt(11) - 5f
            if (incremento){
                dx *= 4f
                dy *= 4f
            }
            var color: Int
            do {
                color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
            } while (color == Color.RED)
            val size = random.nextInt(41) + 20f
            balls.add(Ball(x, y, dx, dy, color, size))
        }

        // Inicialización de la bola principal, asegurándote de que sea roja.
        ball1 = Ball(width / 4f, height / 2f, 5f, 5f, Color.RED, squareSize / 2 + 10)


        /*
        balls.add(Ball(x = width / 2f, y = height / 4f, dx = -4f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = 3f, dy = -5f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = 1f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = -2f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = 3f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = -1f, dy = -6f))*/
        // Agregar más bolas si es necesario
    }
    /* surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int)
         IMPLEMENTACIÓN OPCIONAL SI ES NECESARIO MANEJAR CAMBIOS EN LA SUPERFICIE

       */
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        var a: Int
    }
    /* surfaceDestroyed(holder: SurfaceHolder)
        EN CASO DE QUE LA SUPERFICIE SEA DESTRUIDA POR UN FACTOR EXTERNO
     */
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        playing = false // Detiene el bucle del juego
        while (retry) {
            try {
                gameThread.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace() // Considera manejar este caso adecuadamente
            }
        }
    }
    /* initializeSquares(width: Int, height: Int)
    SE INICIALIZAN LOS "SQUARES" (MATRIZ DE SQUARES) EN FUNCIÓN DEL WIDTH Y EL HEIGHT
     */
    private fun initializeSquares(width: Int,height: Int){
        val numSquaresX = (width / squareSize).toInt()
        val numSquaresY = (height / squareSize).toInt()
        squares = Array(numSquaresX) { Array(numSquaresY) { "WHITE" } }
    }






}