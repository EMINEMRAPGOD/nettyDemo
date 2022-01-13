package com.lyh.netty.netty.echo;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2022/01/10
 * @Description:
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * SimpleChannelInboundHandler:
 *  1. 当channnelRead0方法完成时，已传入的消息并且已处理完它了。当方法返回时，SimpleChannelInboundHandler负责释放指向保存该消息的ByteBuffer的内存引用
 *  2. 而服务端Handler，需要将传入的消息回传给发送者，而write()是异步操作，直接ChannelRead()方法返回后可能仍然没有完成。
 */
public class EchoHandClient extends SimpleChannelInboundHandler<ByteBuf> {
    /**
     * 接收到消息，服务器发送的消息可能会被分块接收(并非一次性全部接收)
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Client received:"+msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!!", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }


}
