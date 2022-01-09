package com.lyh.netty.c4;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.lyh.netty.utils.ByteBufferUtil.debugAll;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2021/12/19
 * @Description:
 */
@Slf4j
public class NioServer {
    public static void main(String[] args) throws IOException {
        String[] data = new String[5];
        String date = new String("4-44-4");
        synchronized(data){

        }
        Integer[] integers = (Integer[]) Arrays.stream(date.split("-")).map(Integer::valueOf).collect(Collectors.toList()).toArray(new Integer[3]);
        //使用nio理解阻塞模式
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //改为非阻塞
        ssc.configureBlocking(false);
        //2.绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        //3.链接集合
        List<SocketChannel> channels = new ArrayList<>();
        while(true){
//            log.debug("connecting...");
            SocketChannel sc = ssc.accept();//非阻塞，线程还会继续运行，如果没有连接建立，但sc是null
            if(sc!=null){
                log.debug("connected...",sc);
                sc.configureBlocking(false);
                channels.add(sc);

            }
            for(SocketChannel channel:channels){
//                log.debug("before read...{}", channel);
                int read = channel.read(buffer);//read方法也是非阻塞方法了，没读到内容会返回0
                if(read > 0){
                    buffer.flip();
                    debugAll(buffer);
                    buffer.clear();
                    log.debug("after read...{}",channel);
                }
            }

        }
    }
}
