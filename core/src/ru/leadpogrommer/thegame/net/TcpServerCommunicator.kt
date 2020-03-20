package ru.leadpogrommer.thegame.net

import com.badlogic.gdx.Gdx
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.serialization.ClassResolvers
import io.netty.handler.codec.serialization.ObjectDecoder
import io.netty.handler.codec.serialization.ObjectEncoder
import ru.leadpogrommer.thegame.Request
import java.util.*

class TcpServerCommunicator(val port: Int): Communicator() {
    private val clients = mutableMapOf<UUID, ChannelHandlerContext>()
    val sendingThread = Thread(Runnable {
        while(true){
            val r = outRequests.take()
            clients[r.uuid]?.writeAndFlush(r)
        }
    }, "SERVER COMMUNICATOR")
    val bossGroup  = NioEventLoopGroup(1)
    val workerGroup = NioEventLoopGroup()
    init {
        val b = ServerBootstrap()
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .childHandler(object: ChannelInitializer<SocketChannel>(){
                    override fun initChannel(ch: SocketChannel?) {
                        val pipeline = ch!!.pipeline()
                        pipeline.addLast(ObjectEncoder(), ObjectDecoder(ClassResolvers.cacheDisabled(null)), RequestReceiverHandler(incomeRequests) {addClient(it)})
                    }

                })
        b.bind(port).sync()
        println("Listening on $port")
        sendingThread.start()
    }



    fun addClient(c: ChannelHandlerContext){
        val cid = UUID.randomUUID()
        clients[cid] = c
        enqueueRequest(Request(cid, "begin", emptyArray()))
        incomeRequests.add(Request(cid, "addPlayer", emptyArray()))
    }

    fun broadcast(request: Request){
        for (c in clients.keys){
            val rc = Request(c, request.endpoint, request.args)
            enqueueRequest(rc)
        }
    }

}