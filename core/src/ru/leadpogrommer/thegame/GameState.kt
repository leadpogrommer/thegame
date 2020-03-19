package ru.leadpogrommer.thegame

import ru.leadpogrommer.thegame.physicalEngine.Entity
import java.io.Serializable
import java.util.*

class GameState : Serializable{
    val entities: MutableMap<UUID, Entity> = mutableMapOf()
}