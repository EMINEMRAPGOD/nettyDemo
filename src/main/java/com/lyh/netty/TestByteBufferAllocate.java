package com.lyh.netty;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.lyh.netty.utils.ByteBufferUtil.debugAll;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2021/12/18
 * @Description:
 */
public class TestByteBufferAllocate {
    public static void main(String[] args) throws IOException {
        System.out.println(ByteBuffer.allocate(16).getClass());//HeapByteBuffer  java 堆内存，读写效率较低，收到垃圾回收的影响
        System.out.println(ByteBuffer.allocateDirect(16).getClass()); //直接内存  读写效率较高（少一次拷贝） 系统内存不受java垃圾回收影响  分配速度比较慢，使用不当则会内存泄漏
        RandomAccessFile rw = new RandomAccessFile("src\\main\\java\\com\\lyh\\netty\\data.txt", "rw");
        FileChannel fileChannel = rw.getChannel();
        ByteBuffer a = ByteBuffer.allocate(10);
        int write = fileChannel.write(a);
        debugAll(a);
        if(a.remaining()>0){
            System.out.println(a.get());
        }

        ByteBuffer source = ByteBuffer.allocate(256);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
//        source.clear();
        source.put("w are you?\n".getBytes());
        split(source);
    }
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
}
