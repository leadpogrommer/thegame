package ru.leadpogrommer.thegame.headless

import org.mockito.Mockito.mock

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import ru.leadpogrommer.thegame.GameServer
import ru.leadpogrommer.thegame.net.TcpServerCommunicator

class ServerWrapper(private val port: Int, private val map: String): ApplicationAdapter() {
    private lateinit var gameServer: GameServer
    override fun create() {
        Gdx.gl = mock(GL30::class.java)
        gameServer = GameServer(map, TcpServerCommunicator(port))
        gameServer.start()
    }

    override fun dispose() {
        gameServer.finish()
        gameServer.join()
    }
}