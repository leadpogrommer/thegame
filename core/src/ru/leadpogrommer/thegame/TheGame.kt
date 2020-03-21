package ru.leadpogrommer.thegame

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ru.leadpogrommer.thegame.net.TcpClientCommunicator
import ru.leadpogrommer.thegame.net.TcpServerCommunicator

class TheGame(private val host:String, private val port: Int, private val serverPort: Int?) : Game() {
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont


    override fun create() {
        if(serverPort != null){
            val srvc = TcpServerCommunicator(serverPort)
            GameServer("test_map/test.tmx", srvc).let {srv -> srvc.setObject(srv) ; srv.start()}
        }

        batch = SpriteBatch()
        font = BitmapFont()

        val lclt = TcpClientCommunicator(host, port)
        val gs = GameScreen(this, "test_map/test.tmx", lclt)

        lclt.setObject(gs)
        setScreen(gs)

    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
    }
}