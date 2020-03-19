package ru.leadpogrommer.thegame.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import ru.leadpogrommer.thegame.TheGame
import kotlinx.cli.*
import ru.leadpogrommer.thegame.GameServer

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val parser = ArgParser("game")
        val serverPort by parser.option(ArgType.Int, fullName = "server")
        parser.parse(arg)


        val config = LwjglApplicationConfiguration()
        LwjglApplication(TheGame(serverPort), config)
    }
}