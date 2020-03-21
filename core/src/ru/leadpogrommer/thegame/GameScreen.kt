package ru.leadpogrommer.thegame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import ru.leadpogrommer.thegame.net.Communicator
import java.util.*

class GameScreen(val game: TheGame, val mapName: String, private val communicator: Communicator): Screen {
    var playerUUID: UUID? = null
    private var state = GameState()

    private val shapeRenderer = ShapeRenderer()
    private val tiledMap = TmxMapLoader().load("test_map/test.tmx")
    private val mapTileSide = tiledMap.properties["tilewidth"] as Int
    private val mapRenderer = OrthogonalTiledMapRenderer(tiledMap, 1f/mapTileSide)
    private val camera = OrthographicCamera()

    // input initialization
    init {
        Gdx.input.inputProcessor = object: InputAdapter(){
            private var playerSpeed = Vector2(0f, 0f)
            val keysMap = mapOf(Pair(Input.Keys.LEFT, Vector2(-1f, 0f)), Pair(Input.Keys.RIGHT, Vector2(1f, 0f)), Pair(Input.Keys.UP, Vector2(0f, 1f)), Pair(Input.Keys.DOWN, Vector2(0f, -1f)))
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
    }

    //camera initialization
    init{
//        shapeRenderer.translate(0., 0.5f, 0f)
    }

    override fun render(delta: Float) {
        communicator.processRequests()
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

        }
        shapeRenderer.end()
    }


    override fun resize(width: Int, height: Int) {
        val vh = (NUM_VERTICAL_TILES).toFloat()
        camera.setToOrtho(false, vh*width/height, vh)
        camera.update()
    }

    override fun hide() {}

    override fun show() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {}


    companion object{
        const val NUM_VERTICAL_TILES = 20
        const val TAG = "GAME SCREEN"
    }
}