package com.lyh.netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2022/01/08
 * @Description:
 */
public class HelloServer {
    public static void main(String[] args) {
        //1启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2.负责连接的事件处理器
                .group(new NioEventLoopGroup())//一开始关心accept事件，
                //3.选择服务器的ServerSocketChannel实现，除此以外有bio，epoll等方式
                .channel(NioServerSocketChannel.class)
                //决定  工作的组件  能进行那些操作
                .childHandler(
                        //代表和客户端进行数据读写的通道初始化，添加别的handler
                        new ChannelInitializer<NioServerSocketChannel>() {
                            @Override
                            protected void initChannel(NioServerSocketChannel ch) throws Exception {
                                //添加具体handler
                                ch.pipeline().addLast(new StringDecoder());

                                ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {

                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                        System.out.println(s);
                                    }
                                });
                            }
                        }
                )
                .bind(14000);
    }
}
