package com.example.ballgame


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.annotation.Keep

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable, View.OnTouchListener {

    //HILO DEL JUEO
    private lateinit var gameThread: Thread
    //BOOLEANO CON EL ESTADO DEL JUEGO (JUGANDO O NO)
    private var playing = false
    //CARACTERIZACIÓN DE LA BOLA
    private val ballPaint = Paint()
    private val ballMainPaint = Paint()
    //CARACTERIZACIÓN DEL SQUARE
    private val squarePaint = Paint()
    //OBJETO BOLA
    private var ball1: Ball? = null
    private var ball2: Ball? = null

    // private val ballList: List<Ball>
    private val balls: MutableList<Ball> = mutableListOf()
    private var score = 0 // Inicializamos el puntaje en 0

    //TAMAÑO DE CADA SQUARE
    private val squareSize = 20f
    //CONJUNTO DE SQUARES
    private var squares: Array<Array<String>>? = null

    /* init{}
    ES UNA ESPECIE DE CONSTRUCTOR, PERO CON OTROS MATICES.
    https://stackoverflow.com/questions/55356837/what-is-the-difference-between-init-block-and-constructor-in-kotlin
    PODREMOS INICIALIZAR LA BOLA Y EL “SQUARE” QUE HABREMOS DEFINIDO COMO OBJETOS DE LA CLASE PAINT() USANDO LA CLASE COLOR()
     */
    init {
        holder.addCallback(this)
        //Asignar color a ball y square

        ballPaint.color = Color.BLACK
        ballMainPaint.color = Color.RED
        squarePaint.color = Color.WHITE

        setOnTouchListener(this)
    }

    //https://developer.android.com/reference/kotlin/android/view/View.OnTouchListener

    // Método onTouch para manejar los eventos de toque
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            // Obtener las coordenadas del toque
            val touchX = event.x
            val touchY = event.y

            // Calcular la dirección de la bola más cercana al toque
            ball1?.let { ball1 ->

                // Mover la primera bola hacia el punto de toque
                val angle = Math.atan2((touchY - ball1.y).toDouble(), (touchX - ball1.x).toDouble())
                ball1.dx = (5 * Math.cos(angle)).toFloat()
                ball1.dy = (5 * Math.sin(angle)).toFloat()
            }
        }
        return true
    }

    private fun checkCollisionWithList(ball: Ball, balls: MutableList<Ball>) {
        balls.forEach { otherBall ->
            if (ball !== otherBall) { // Asegurarse de que no estamos comparando la misma bola
                val distanceX = ball.x - otherBall.x
                val distanceY = ball.y - otherBall.y
                val distance = Math.sqrt((distanceX * distanceX + distanceY * distanceY).toDouble()).toFloat()

                if (distance < ball.radius + otherBall.radius) {
                    // Hay colisión entre la bola y otra bola en la lista, invertir direcciones
                    balls.remove(otherBall)
                    invalidate()

                }
            }
        }
    }


    /* run()
    SERÁ UN MÉTODO QUE SE EJECUTARÁ DE FORMA “INFINITA” HASTA QUE OCURRA ALGO EN EL JUEGO
    -->> ¿QUE PUEDE OCURRIR EN UNA INTERFAZ COMO LA QUE HABÉIS VISTO?. <<--
    PODREMOS DEFINIR ALGÚN TIPO DE VARIABLE BOOLEANA (PLAYING POR EJEMPLO).
    LLAMAREMOS DE FORMA ININTERRUMPIDA A LOS MÉTODOS UPDATE(), DRAW() Y CONTROL().
    */
    override fun run() {
        while (playing) {
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
    private fun update() {

        ball1?.updatePosition(true)
        balls.forEach { it.updatePosition(false) }
        ball1?.let { checkCollisionWithList(it, balls) }

    }

    /* draw()
    EN CASO DE QUE EL "HOLDER SURFACE" (SOPORTE DE SUPERFICIE) CONSTRUIDO SEA CORRECTO,
    PINTA LOS SQUARES Y LA BOLA MEDIANTE CANVAS.
    BLOQUEO Y DESBLOQUEO MIENTRAS SE PINTA.
     */
    private fun draw() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            drawSquares(canvas)
            drawScore(canvas) // Dibujar el puntaje
            balls.forEach { drawBall(canvas, it) }
            ball1?.let { drawBall1(canvas, it) }
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun drawScore(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 50f // Tamaño del texto del puntaje

        val text = "Score: $score" // Texto del puntaje
        canvas.drawText(text, 50f, 100f, paint) // Dibujar el puntaje en la posición especificada
    }
    /* control()
    ¿QUE OS PUEDO CONTAR YO DE THREADS QUE NO OS HAYA CONTADO DIEGO? :-)
     */
    private fun control() {
        try {
            Thread.sleep(17) // Aproximadamente 60 FPS
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /* drawBall()
        PINTAMOS UN CIRCULO CON LAS CARACTERÍSTICAS DE LA BOLA (QUIEN PINTA ES LA CLASE CANVA)
    */
    private fun drawBall(canvas: Canvas, ball: Ball) {
        canvas.drawCircle(ball.x, ball.y, ball.radius, ballPaint)

    }

    private fun drawBall1(canvas: Canvas, ball: Ball) {
        canvas.drawCircle(ball.x, ball.y, ball.radius, ballMainPaint)

    }

    /* drawSquares()
        PINTAMOS RECTÁNGULOS ¿EN UNA MATRIZ?
        ESTAMOS RELLENANDO EL "CONJUNTO DE SQUARES" QUE DEFINIMOS AL PRINCIPIO
     */
    private fun drawSquares(canvas: Canvas) {
        squares?.let { squaresArray ->
            for (i in squaresArray.indices) {
                for (j in squaresArray[i].indices) {

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
        gameThread.join()
    }

    /* resume()
        EN CASO DE REANUDAR EL JUEGO, TRAS ALGÚN SUCESO O EVENTO EXTERNO.
    */
    fun resume() {
        playing = true
        gameThread = Thread(this)
        gameThread.start()
    }

    /* DEFINIERMOS UNA INNER CLASS:
    https://wiki.yowu.dev/es/Knowledge-base/Kotlin/Learning/043-nested-and-inner-classes-in-kotlin-creating-classes-within-classes
    ¿CUALES SON LOS ATRIBUTOS DE LA INNER CLASS?

    ESTA CLASE TIENE UN ÚNICO MÉTODO QUE ACTUALIZA LA POSICIÓN DE LA BOLA Y COMPRUEBA SI HAY COLISIÓN.
    */

    inner class Ball(var x: Float, var y: Float, var dx: Float = 5f, var dy: Float = 5f) {
        val radius = squareSize / 2 +10

        fun updatePosition(redBall: Boolean) {
            x += dx
            y += dy
            checkCollision(this, redBall)
        }
    }
    /* checkCollision(Ball)
        EN CASO DE COLISIÓN, DEBEMOS CAMBIAR EL SENTIDO DE LA BOLA
        TENEMOS QUE TENER CLARO QUE LA BOLA PUEDE REBOTAR ARRIBA, ABAJO, IZQUIERDA Y DERECHA,
        ES DECIR, EJE X E Y.
        ADEMÁS LA BOLA AL REBOTAR, NO DEBERÍA DEJAR DE VERSE.
     */
    private fun checkCollision(ball: Ball, redBall: Boolean) {
        if (ball.x - ball.radius < 0 || ball.x + ball.radius > width) {
            ball.dx = -ball.dx
            if (redBall) score++
        }
        if (ball.y - ball.radius < 0 || ball.y + ball.radius > height) {
            ball.dy = -ball.dy
            if (redBall) score++
        }
    }

    /* surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int)
       IMPLEMENTACIÓN OPCIONAL SI ES NECESARIO MANEJAR CAMBIOS EN LA SUPERFICIE

     */

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        var a: Int
    }
    /* surfaceCreated(holder: SurfaceHolder)
       SE UBICA-POSICIONA EL OBJETO BOLA Y SE INICIALIZAN LOS "SQUARES"
    */
    override fun surfaceCreated(holder: SurfaceHolder) {
        ball1 = Ball(x = width / 4f, y = height / 2f)
        initializeBalls()

        initializeSquares(width, height)
        resume() // Comenzar el juego cuando la superficie esté creada
    }
    private fun initializeBalls() {
        balls.add(Ball(x = width / 2f, y = height / 4f, dx = -4f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = 3f, dy = -5f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = 1f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = -2f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = 3f, dy = -6f))
        balls.add(Ball(x = width / 3f, y = height / 3f, dx = -1f, dy = -6f))
        // Agregar más bolas si es necesario
        // No son formas de agregar bolas a la partida, pero es que tenía "prisa"
    }
    /* surfaceDestroyed(holder: SurfaceHolder)
       EN CASO DE QUE LA SUPERFICIE SEA DESTRUIDA POR UN FACTOR EXTERNO
    */
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        pause() // Pausar el juego cuando la superficie sea destruida
    }

    /* initializeSquares(width: Int, height: Int)
    SE INICIALIZAN LOS "SQUARES" (MATRIZ DE SQUARES) EN FUNCIÓN DEL WIDTH Y EL HEIGHT
     */
    private fun initializeSquares(width: Int, height: Int) {
        val numSquaresX = (width / squareSize).toInt()
        val numSquaresY = (height / squareSize).toInt()
        squares = Array(numSquaresX) { Array(numSquaresY) { "WHITE" } }
    }
}
