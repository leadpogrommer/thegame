package ru.leadpogrommer.thegame

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ru.leadpogrommer.thegame.net.TcpClientCommunicator
import ru.leadpogrommer.thegame.net.TcpServerCommunicator

class TheGame(private val host:String, private val port: Int, private val serverPort: Int?) : Game() {
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont
    lateinit var gameServer: GameServer
    lateinit var gameScreen: GameScreen


    override fun create() {
        if(serverPort != null){
            val srvc = TcpServerCommunicator(serverPort)
            gameServer =  GameServer("test_map/test.tmx", srvc)
            gameServer.start()
        }

        batch = SpriteBatch()
        font = BitmapFont()

        val lclt = TcpClientCommunicator(host, port)
        gameScreen = GameScreen(this, "test_map/test.tmx", lclt)

        lclt.setObject(gameScreen)
        setScreen(gameScreen)

    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        serverPort?:return
        gameScreen.dispose()
        gameServer.finish()
        gameServer.join()
    }
}