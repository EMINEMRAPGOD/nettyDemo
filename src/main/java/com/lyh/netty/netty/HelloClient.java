package com.lyh.netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2022/01/08
 * @Description:
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        new Bootstrap()
                //客户端对服务器发来的事件进行处理
                .group(new NioEventLoopGroup())
                //选择客户端channel实现
                .channel(NioSocketChannel.class)
                //添加处理器，初始化后调用
                .handler(new ChannelInitializer<Channel>() {
                    @Override  //在连接后调用
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());

                    }
                })
                .connect(new InetSocketAddress("localhost",14000))
                .sync()//阻塞方法直到连接建立
                .channel()
                .writeAndFlush("hello,world");
    }
}
