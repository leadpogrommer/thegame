package ru.leadpogrommer.thegame.net

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import ru.leadpogrommer.thegame.Request
import java.util.concurrent.LinkedBlockingQueue

class RequestReceiverHandler (private val rq: LinkedBlockingQueue<Request>, val onReady: (ChannelHandlerContext)->Unit): ChannelInboundHandlerAdapter(){
    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        rq.put(msg as Request)
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        onReady(ctx!!)
    }
}