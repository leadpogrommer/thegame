package ru.leadpogrommer.thegame

import com.badlogic.gdx.math.Vector2
import ru.leadpogrommer.thegame.physicalEngine.Entity
import java.util.*

class Player(id: UUID): Entity(id, speed = Vector2(0f, 0f), pos = Vector2(0f, 0f), radius = 1f) {
    var name: String = "__no_name__";
}