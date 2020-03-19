package ru.leadpogrommer.thegame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import ru.leadpogrommer.thegame.net.Communicator
import ru.leadpogrommer.thegame.net.LocalServerCommunicator
import ru.leadpogrommer.thegame.physicalEngine.Entity
import ru.leadpogrommer.thegame.physicalEngine.PhysicsEngine
import java.lang.System.currentTimeMillis
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue


class GameServer(val mapName: String, val port: Int, private val communicator: Communicator): Thread("GAME SERVER") {
    private val engine = PhysicsEngine()
    private val methods = mutableMapOf<String, Any>()
    private val requestQueue: Queue<Request> = ConcurrentLinkedQueue();
    private val state = GameState()

    override fun run() {
        Gdx.app.log(name, "Server started at port $port on map $mapName")

        var prevTickTime = currentTimeMillis()
        while (true){
            tick(currentTimeMillis() - prevTickTime)
            prevTickTime = currentTimeMillis()
            communicator.processRequests()
            (communicator as LocalServerCommunicator).broadcast(Request(UUID.randomUUID(), "updateState", arrayOf(state)))
            sleep(10)

        }
    }

    private fun tick(d: Long){
        val delta: Float = d / 100f
        for (entity in state.entities.values){
            entity.pos.x += entity.speed.x * delta
            entity.pos.y += entity.speed.y * delta
        }
    }


    @Endpoint()
    fun setMovementVector(r: Request){
        val newVector = Vector2((r.args[0] as Float), (r.args[1] as Float))
        if (newVector.len() > 1f){
            newVector.nor()
        }
        newVector.scl(PLAYER_MAX_SPEED)
        state.entities[r.uuid]?.let { it.speed =  newVector}
    }

    @Endpoint()
    fun addPlayer(r: Request){
        state.entities[r.uuid] = Player(r.uuid)
    }


    companion object{
        const val PLAYER_MAX_SPEED = 4f
    }
}