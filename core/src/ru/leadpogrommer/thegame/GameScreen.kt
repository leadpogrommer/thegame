package ru.leadpogrommer.thegame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import ru.leadpogrommer.thegame.net.Communicator
import ru.leadpogrommer.thegame.net.Request
import ru.leadpogrommer.thegame.net.Endpoint
import java.util.*

class GameScreen(val game: TheGame, val mapName: String, private val communicator: Communicator): Screen {
    var playerUUID: UUID? = null
    private var state = GameState()

    private val shapeRenderer = ShapeRenderer()
    private val tiledMap = TmxMapLoader().load("test_map/test.tmx")
    private val mapTileSide = tiledMap.properties["tilewidth"] as Int
    private val mapRenderer = OrthogonalTiledMapRenderer(tiledMap, 1f/mapTileSide)
    private val camera = GameCamera(tiledMap.properties["width"] as Int, tiledMap.properties["height"] as Int)
    private val renderBeforePlayer = mutableListOf<Int>()
    private val renderAfterPlayer = mutableListOf<Int>()

    // input initialization
    init {
        Gdx.input.inputProcessor = object: InputAdapter(){
            private var playerSpeed = Vector2(0f, 0f)
            val keysMap = mapOf(Pair(Input.Keys.A, Vector2(-1f, 0f)), Pair(Input.Keys.D, Vector2(1f, 0f)), Pair(Input.Keys.W, Vector2(0f, 1f)), Pair(Input.Keys.S, Vector2(0f, -1f)))
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

            override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
                val angle = camera.getFacingAngle(screenX.toFloat(), Gdx.graphics.height - screenY.toFloat())
                sendAngle(angle)
                return true
            }

            fun sendAngle(angle: Float){
                playerUUID?:return
                communicator.enqueueRequest(Request(playerUUID!!, "setAngle", arrayOf(angle)))
            }

            fun sendSpeed(){
                communicator.enqueueRequest(Request(playerUUID!!, "setMovementVector", arrayOf(playerSpeed.x, playerSpeed.y)))
            }
        }
    }


    override fun render(delta: Float) {
        communicator.processRequests()
        playerUUID?:return
        state.entities[playerUUID!!]?:return
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position(state.entities[playerUUID!!]!!.pos)
        camera.update()
        mapRenderer.setView(camera)
        mapRenderer.render()



        renderEntities()
    }

    @Endpoint
    fun begin(r: Request){
        playerUUID = r.uuid
    }

    @Endpoint
    fun updateState(r: Request){
        state = r.args[0] as GameState
    }

    private fun renderEntities(){
        shapeRenderer.color = Color.GREEN
        shapeRenderer.projectionMatrix = camera.combined

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        for (entity in state.entities.values){
            shapeRenderer.circle(entity.pos.x, entity.pos.y, entity.radius)
            val facingVect = Vector2(1f, 0f).setLength(1f).setAngleRad(entity.facing)
            shapeRenderer.circle(entity.pos.x + facingVect.x, entity.pos.y + facingVect.y, 0.2f)

        }
        shapeRenderer.end()
    }


    override fun resize(width: Int, height: Int) {
        val vh = (NUM_VERTICAL_TILES).toFloat()
        camera.setToOrtho(false, vh*width/height, vh)
        camera.translate(1f, 0f)
        camera.update()
    }

    override fun hide() {}

    override fun show() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        communicator.stop()
    }


    companion object{
        const val NUM_VERTICAL_TILES = 20
        const val TAG = "GAME SCREEN"
    }
}