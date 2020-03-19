package ru.leadpogrommer.thegame.net

import ru.leadpogrommer.thegame.Request


class LocalClientCommunicator (val server: LocalServerCommunicator): Communicator(){
    init {
        server.addClient(this)
    }
    override fun enqueueRequest(r: Request) {
        server.incomeRequests.add(r)
    }


}