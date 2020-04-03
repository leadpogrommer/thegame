package ru.leadpogrommer.thegame.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ru.leadpogrommer.thegame.TheGame
import kotlinx.cli.*
import ru.leadpogrommer.thegame.GameServer
import ru.leadpogrommer.thegame.net.TcpServerCommunicator

object DesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val parser = ArgParser("game")
        val serverPort by parser.option(ArgType.Int, fullName = "create-server")
        val connectTo by parser.option(ArgType.String, fullName = "connect-to")
        parser.parse(args)

        val config = Lwjgl3ApplicationConfiguration()


        val host = connectTo!!.split(':')
        Lwjgl3Application(TheGame(host[0], host[1].toInt(), serverPort), config)
    }
}