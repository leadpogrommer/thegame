package ru.leadpogrommer.thegame.net

import ru.leadpogrommer.thegame.Request
import java.util.*

class LocalServerCommunicator(): Communicator() {
    fun addClient(c: LocalClientCommunicator){
        val cid = UUID.randomUUID()
        clients[cid] = c
        enqueueRequest(Request(cid, "begin", emptyArray()))
        incomeRequests.add(Request(cid, "addPlayer", emptyArray()))
    }
    val clients = mutableMapOf<UUID, LocalClientCommunicator>()
    override fun enqueueRequest(r: Request) {
        clients[r.uuid]?.incomeRequests?.add(r)
    }
    fun broadcast(request: Request){
        for (c in clients.values){
            c.incomeRequests.add((request))
        }
    }

}