package ru.leadpogrommer.thegame.net

import ru.leadpogrommer.thegame.Endpoint
import ru.leadpogrommer.thegame.Request
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation

abstract class Communicator() {
    val outRequests: LinkedBlockingQueue<Request> = LinkedBlockingQueue<Request>()
    val incomeRequests = LinkedBlockingQueue<Request>()
    private val methods: MutableMap<String, KFunction<*>> = mutableMapOf()
    private lateinit var obj: Any


    fun setObject(o: Any){
        obj = o
        methods.clear()
        for( function in obj::class.declaredFunctions.filter { it.findAnnotation<Endpoint>() != null }){
            methods[function.name] = function
        }
    }

    // Send
    open fun enqueueRequest(r: Request){
        outRequests.add(r)
    }

    // Receive
    fun processRequests(){
        while(!incomeRequests.isEmpty()){
            val request = incomeRequests.poll()
            methods[request.endpoint]!!.call(obj, request)
        }
    }


}