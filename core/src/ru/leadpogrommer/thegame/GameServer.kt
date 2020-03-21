package ru.leadpogrommer.thegame

import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import ru.leadpogrommer.thegame.net.Communicator
import ru.leadpogrommer.thegame.net.TcpServerCommunicator
import ru.leadpogrommer.thegame.net.Endpoint
import ru.leadpogrommer.thegame.physicalEngine.PhysicsEngine
import ru.leadpogrommer.thegame.net.Request
import java.lang.System.currentTimeMillis
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue


class GameServer(mapName: String, private val communicator: Communicator): Thread("GAME SERVER") {
    private val methods = mutableMapOf<String, Any>()
    private val requestQueue: Queue<Request> = ConcurrentLinkedQueue<Request>()
    private val state = GameState()
    private val tiledMap = TmxMapLoader().load(mapName)
    private val engine = PhysicsEngine(tiledMap, state.entities.values)

    override fun run() {
        var prevTickTime = currentTimeMillis()
        while (true){
            engine.tick(delta = (currentTimeMillis() - prevTickTime)/1000f)
            prevTickTime = currentTimeMillis()
            communicator.processRequests()
            (communicator as TcpServerCommunicator).broadcast(Request(UUID.randomUUID(), "updateState", arrayOf(state)))
            sleep(10)

        }
    }

    @Endpoint
    fun setMovementVector(r: Request){
        val newVector = Vector2((r.args[0] as Float), (r.args[1] as Float))
        if (newVector.len() > 1f){
            newVector.nor()
        }
        newVector.scl(PLAYER_MAX_SPEED)
        state.entities[r.uuid]?.let { it.speed =  newVector}
    }

    @Endpoint
    fun addPlayer(r: Request){
        state.entities[r.uuid] = Player(r.uuid)
    }


    companion object{
        const val PLAYER_MAX_SPEED = 4f
    }
}