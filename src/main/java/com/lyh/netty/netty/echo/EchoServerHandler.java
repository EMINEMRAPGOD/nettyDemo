package com.lyh.netty.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2022/01/10
 * @Description:
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws FileNotFoundException {
        //ReferenceCountUtil.release(msg);
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("Server recevied:"+ buf.toString(Charset.forName("UTF-8")));
        ctx.write(buf);
    }



    final ByteBuf byteBuffer = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("hello world!\r\n", CharsetUtil.UTF_8));

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ChannelFuture cf= ctx.writeAndFlush(byteBuffer.duplicate());
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()){
                    System.out.println("Write successful!");
                }else{
                    System.out.println("Write Error!");
                    future.cause().printStackTrace();
                }
            }
        });
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE); //将未决消息冲刷到远程节点且关闭该Channel
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }



}

