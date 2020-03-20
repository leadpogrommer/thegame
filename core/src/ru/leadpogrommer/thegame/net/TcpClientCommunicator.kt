package ru.leadpogrommer.thegame.net

import com.badlogic.gdx.Gdx
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import ru.leadpogrommer.thegame.Request
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.serialization.ClassResolver
import io.netty.handler.codec.serialization.ClassResolvers
import io.netty.handler.codec.serialization.ObjectDecoder
import io.netty.handler.codec.serialization.ObjectEncoder


class TcpClientCommunicator (val host: String, val port: Int): Communicator(){
    lateinit var serverContext: ChannelHandlerContext
    val sendingThread = Thread(Runnable {
        while(true){
            val r = outRequests.take()
            serverContext.writeAndFlush(r)
        }
    }, "CLIENT COMMUNICATOR")
    val clientEventLoopGroup = NioEventLoopGroup()
    init {
        val b = Bootstrap()
        b.group(clientEventLoopGroup)
                .channel(NioSocketChannel::class.java)
                .handler(object : ChannelInitializer<SocketChannel>(){
                    override fun initChannel(ch: SocketChannel?) {
                        val pipeline = ch!!.pipeline()
                        pipeline.addLast(ObjectEncoder(), ObjectDecoder(ClassResolvers.cacheDisabled(null)), RequestReceiverHandler(incomeRequests) {serverContext = it})                    }

                })
        b.connect(host, port).sync()
        println("Connected to $host:$port")
        sendingThread.start()
    }


}