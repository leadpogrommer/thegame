package ru.leadpogrommer.thegame

import com.badlogic.gdx.math.Vector2
import ru.leadpogrommer.thegame.physicalEngine.Entity
import ru.leadpogrommer.thegame.physicalEngine.MapLayer
import java.util.*

class Bullet(uuid: UUID, speed: Vector2, pos: Vector2, facing: Float, val owner: UUID): Entity(uuid, speed, pos, 0.1f, facing) {
    override var layer = MapLayer.AIR
    override fun onCollide(e: Entity?) {
        destroyed = true
    }

}