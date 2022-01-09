package com.lyh.netty.selector;

import com.lyh.netty.utils.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;

import static com.lyh.netty.utils.ByteBufferUtil.debugAll;
import static com.lyh.netty.utils.ByteBufferUtil.debugRead;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2022/01/04
 * @Description:
 */
@Slf4j
public class SelectServer {
    private static void split(ByteBuffer source){
        source.flip();
        for (int i=0;i<source.limit();i++){
            if(source.get(i)=='\n'){
                int length = i+1-source.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                for(int j=0;j<length;j++){
                    target.put(source.get());
                }
                debugAll(target);
            }
        }
        source.compact();
    }
    public static void main(String[] args) throws IOException {
        //1.创建selector,管理多个channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        //2.建立selector和channel地联系(注册)
        //SelectionKey就是将来事件发生后，通过它可以知道事件和哪个channel的事件
        SelectionKey ssckey = ssc.register(selector,0,null);
        log.debug("register key:{}",ssckey);
        //key 只关注accept事件
        ssckey.interestOps(SelectionKey.OP_ACCEPT);

        ssc.bind(new InetSocketAddress(8080));
        while (true){
            //3.select 方法，没有事件发生，线程阻塞，有事件，线程才会恢复运行
            // select当事件未处理时或取消时，不会阻塞。
            selector.select();
            //4.处理事件，selectedKeys

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                log.debug("key:{}",key);
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    log.debug("{}",sc);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    SelectionKey register = sc.register(selector, 0, buffer);
                    register.interestOps(SelectionKey.OP_READ);
                    log.debug("{}",sc);
                }else if(key.isReadable()){

                    try {
                        SocketChannel channel = (SocketChannel) key.channel();//拿到触发事件的channel
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);
                        if(read==-1){
                            key.cancel();

                        }else{
                            split(buffer);
                            if(buffer.position()==buffer.limit()){
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity()*2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();  //客户端断开，所以需要将key取消
                    }

                }

            }


        }
    }
}
