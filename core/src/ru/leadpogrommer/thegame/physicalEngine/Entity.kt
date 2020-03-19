package ru.leadpogrommer.thegame.physicalEngine

import com.badlogic.gdx.math.Vector2
import java.io.Serializable
import java.util.*

open class Entity(var uuid: UUID, var speed: Vector2, var pos: Vector2, var radius: Float): Serializable{
    var destroyed = false
    open fun onCollide(e: Entity?){}
}