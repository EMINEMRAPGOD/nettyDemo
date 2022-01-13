package com.lyh.netty.netty.echo;

import com.lyh.netty.netty.echo.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class EchoServer {

    public static void main(String[] args) throws InterruptedException {
        EchoServer echoNettyServer =  new EchoServer();
        echoNettyServer.start(8989);
    }

    public void start(int port) throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        final ByteBuf byteBuffer = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("HI!\r\n", Charset.forName("UTF-8")));

        EventLoopGroup group = new NioEventLoopGroup(); //非阻塞方式
        try{
            ServerBootstrap b = new ServerBootstrap(); //创建ServerBootstrap
            b.group(group)//NioSctpChannel
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }//每个已接收的连接都调用它

                    });

            ChannelFuture f = b.bind().sync();  //异步绑定服务器，调用sync()方法阻塞等待直接绑定完成
            f.channel().closeFuture().sync(); //获取Channel的CloseFuture,并且阻塞当前线程直接它完成
        }finally {
            group.shutdownGracefully().sync();
        }
    }


}
