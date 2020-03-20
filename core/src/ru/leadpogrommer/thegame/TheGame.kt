package ru.leadpogrommer.thegame

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ru.leadpogrommer.thegame.net.TcpClientCommunicator

class TheGame(private val host:String, private val port: Int) : Game() {
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont


    override fun create() {


        batch = SpriteBatch()
        font = BitmapFont()

        val lclt = TcpClientCommunicator(host, port)
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