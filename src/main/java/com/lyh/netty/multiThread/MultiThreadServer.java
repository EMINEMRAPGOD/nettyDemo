package com.lyh.netty.multiThread;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lyh.netty.utils.ByteBufferUtil.debugAll;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2022/01/07
 * @Description:
 */
@Slf4j
public class MultiThreadServer {
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



        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        ssc.register(boss, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        //创建固定数量的worker
        Worker worker = new Worker("woker-0");
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        AtomicInteger index = new AtomicInteger(0);
        while(true){
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while(iter.hasNext()){
                SelectionKey key = iter.next();
                iter.remove();
                if(key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.debug("connected...{}",sc.getRemoteAddress());
                    //关联selector
                    log.debug("before register...{}",sc.getRemoteAddress());
                    workers[index.getAndIncrement() % workers.length].register(sc);
//                    worker.register(sc);
//                    sc.register(worker.selector,SelectionKey.OP_READ,null);
                    log.debug("after register...{}",sc.getRemoteAddress());

                }
            }
        }
    }
    static class Worker implements Runnable{
        private Thread thread;
        private Selector selector;
        private String name;
        private volatile  boolean start = false;
        ConcurrentLinkedDeque<Runnable> que = new ConcurrentLinkedDeque<>();
        public Worker(String name){
            this.name = name;
        }
        public void register(SocketChannel sc) throws IOException{
            if(!start) {

                selector = Selector.open();
                thread = new Thread(this, name);
                thread.start();
                start = true;
            }
            que.add(()->{
                try {
                    sc.register(selector,SelectionKey.OP_READ,null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }

            });
            selector.wakeup();
        }

        @Override
        public void run() {
            while (true){
                try {
                    selector.select();
                    Runnable task = que.poll();
                    if(task!=null){
                        task.run();
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if(key.isReadable()){
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel)key.channel();
                            log.debug("read...{}",channel.getRemoteAddress());
                            channel.read(buffer);
                            buffer.flip();
                            debugAll(buffer);
                        }else if(key.isWritable()){

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
