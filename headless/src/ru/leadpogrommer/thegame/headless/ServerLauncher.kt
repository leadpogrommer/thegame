package ru.leadpogrommer.thegame.headless

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlin.system.exitProcess

object ServerLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val parser = ArgParser("game")
        val serverPort by parser.option(ArgType.Int, fullName = "port")
        parser.parse(arg)
        if(serverPort == null){
            println("ERROR")
            exitProcess(-1)
        }

        val config = HeadlessApplicationConfiguration()
        HeadlessApplication(ServerWrapper(serverPort!!, "test_map/test.tmx"), config)
    }
}