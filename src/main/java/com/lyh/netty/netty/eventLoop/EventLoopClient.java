package com.lyh.netty.netty.eventLoop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2022/01/08
 * @Description:
 */
@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        // future异步非阻塞 获取结果需要addListener 或者调用sync同步等待
        final ChannelFuture future = new Bootstrap().group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 真正执行connect的是nio线程
                .connect("localhost", 8989);
        // 不调用sync() 下面打印的channel [id: 0x435488cf]只包含id 说明么有建立好连接 数据发不出的
        // future.sync();
        future.addListener((ChannelFutureListener) future1 -> {
            final Channel channel = future1.channel();
            channel.writeAndFlush("宝贝");
            log.debug("{}", channel);
        });
        // final Channel channel = future.channel();
        // channel.writeAndFlush("宝贝");
        // log.debug("{}", channel);
        System.out.println();
    }

}
