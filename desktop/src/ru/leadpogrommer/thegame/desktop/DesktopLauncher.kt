package ru.leadpogrommer.thegame.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
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

        val config = Lwjgl3ApplicationConfiguration()
        Lwjgl3Application(TheGame("127.0.0.1", 1337, serverPort), config)
    }
}