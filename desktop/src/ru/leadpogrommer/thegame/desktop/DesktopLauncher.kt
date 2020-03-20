package ru.leadpogrommer.thegame.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import ru.leadpogrommer.thegame.TheGame
import kotlinx.cli.*
import ru.leadpogrommer.thegame.GameServer
import ru.leadpogrommer.thegame.net.TcpServerCommunicator

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val parser = ArgParser("game")
        val serverPort by parser.option(ArgType.Int, fullName = "server")
        parser.parse(arg)

        if(serverPort != null){
            val srvc = TcpServerCommunicator(serverPort!!)
            GameServer("test_map/test.tmx", srvc).let {srv -> srvc.setObject(srv) ; srv.start()}
        }


        val config = LwjglApplicationConfiguration()
        LwjglApplication(TheGame("127.0.0.1", 1337), config)
    }
}