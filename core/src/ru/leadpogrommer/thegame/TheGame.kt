package ru.leadpogrommer.thegame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import ru.leadpogrommer.thegame.net.LocalClientCommunicator
import ru.leadpogrommer.thegame.net.LocalServerCommunicator

class TheGame(private val serverPort: Int?) : Game() {
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont


    override fun create() {


        batch = SpriteBatch()
        font = BitmapFont()

        val lsrv = LocalServerCommunicator()
        val lclt = LocalClientCommunicator(lsrv)

        serverPort?.let { GameServer("test_map/test.tmx", it, lsrv).let {srv -> lsrv.setObject(srv) ; srv.start()} }


        val gs = GameScreen(this, "test_map/test.tmx", lclt)

        lclt.setObject(gs)
        setScreen(gs)

    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
    }
}