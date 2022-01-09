package com.lyh.netty.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.lyh.netty.utils.ByteBufferUtil.debugAll;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2021/12/19
 * @Description:
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        //使用nio理解阻塞模式
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();

        //2.绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        //3.链接集合
        List<SocketChannel> channels = new ArrayList<>();
        while(true){
            log.debug("connecting...");
            SocketChannel sc = ssc.accept();//阻塞方法，没法获取线程时，当前线程停止执行
            log.debug("connected...",sc);
            channels.add(sc);
            for(SocketChannel channel:channels){
                log.debug("before read...{}", channel);
                channel.read(buffer);//阻塞方法，没读到内容，当前线程停止运行
                buffer.flip();
                debugAll(buffer);
                buffer.clear();
                log.debug("after read...{}",channel);
            }
        }
    }
}
