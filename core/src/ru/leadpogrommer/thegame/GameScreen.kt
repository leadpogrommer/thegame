package ru.leadpogrommer.thegame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import ru.leadpogrommer.thegame.net.Communicator
import java.util.*

class GameScreen(val game: TheGame, val map: String, private val communicator: Communicator): Screen {
    var playerUUID: UUID? = null
    var state = GameState()
    private val camera = OrthographicCamera()
    private val shapeRenderer = ShapeRenderer()


    init {
        Gdx.input.inputProcessor = object: InputAdapter(){
            private var playerSpeed = Vector2(0f, 0f)
            val keysMap = mapOf<Int, Vector2>(Pair(Input.Keys.LEFT, Vector2(-1f, 0f)), Pair(Input.Keys.RIGHT, Vector2(1f, 0f)), Pair(Input.Keys.UP, Vector2(0f, 1f)), Pair(Input.Keys.DOWN, Vector2(0f, -1f)))
            override fun keyDown(keycode: Int): Boolean {
                if(keycode in keysMap.keys){
                    playerSpeed.add(keysMap[keycode])
                }
                sendSpeed()
                return true
            }

            override fun keyUp(keycode: Int): Boolean {
                if(keycode in keysMap.keys){
                    playerSpeed.sub(keysMap[keycode])
                }
                sendSpeed()
                return true
            }

            fun sendSpeed(){
                communicator.enqueueRequest(Request(playerUUID!!, "setMovementVector", arrayOf(playerSpeed.x, playerSpeed.y)))
            }
        }

        camera.setToOrtho(false, 300f, 300f)
    }

    override fun render(delta: Float) {
        communicator.processRequests()

        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.color = Color.GREEN
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (entity in state.entities.values){
            shapeRenderer.circle(entity.pos.x, entity.pos.y, entity.radius)
        }
        shapeRenderer.end()

        Gdx.app.log("GAME SCREEN", "${state.entities[playerUUID]?.pos}")

    }

    @Endpoint()
    fun begin(r: Request){
        playerUUID = r.uuid
    }

    @Endpoint()
    fun updateState(r: Request){
        state = r.args[0] as GameState
    }


    override fun resize(width: Int, height: Int) {}

    override fun hide() {}

    override fun show() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {}
}