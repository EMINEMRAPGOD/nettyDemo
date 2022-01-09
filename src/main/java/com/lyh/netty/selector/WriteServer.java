package com.lyh.netty.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.jar.Pack200;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2022/01/05
 * @Description:
 */
public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));

        while(true){
            int select = selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey sckey = sc.register(selector, 0, null);
                    //1.想客户端发送大量数据
                    StringBuilder sb = new StringBuilder();
                    for(int i=0;i<300000;i++){
                        sb.append("a");
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    //2.返回值代表实际写入的字节数
                    //如果文件太大，会卡在此处不能发送完，变成非阻塞模式----卡在当前serversocket
                        int write = sc.write(buffer);
                        System.out.println(write);
                    if(buffer.hasRemaining()){
                        //关注读写事件 ，避免原本为读的事件被覆盖掉
                        sckey.interestOps(sckey.interestOps()+SelectionKey.OP_WRITE);
                        //把未写完的数据写到sckey上
                        sckey.attach(buffer);
                    }

                }else if(key.isWritable()){
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel channel = (SocketChannel) key.channel();
                    int write = channel.write(buffer);
                    System.out.println(write);
                    //6.清理操作
                    if(!buffer.hasRemaining()){
                        //清除buffer
                        key.attach(null);
                        //不需要关注write事件
                        key.interestOps(key.interestOps()-SelectionKey.OP_WRITE);
                    }
                }
            }
        }
    }
}
