package com.lyh.netty;

import sun.awt.windows.WBufferStrategy;

import java.nio.ByteBuffer;

import static com.lyh.netty.utils.ByteBufferUtil.debugAll;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2021/12/18
 * @Description:
 */
public class TestByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer allocate = ByteBuffer.allocate(10);
        allocate.put((byte) 0x61);
        debugAll(allocate);
        allocate.put(new byte[] {0x62,0x63,0x64
        } );
        debugAll(allocate);
        System.out.println(allocate.get());
        allocate.flip();
        System.out.println(allocate.get());
        allocate.compact();
        debugAll(allocate);
//        allocate.clear();
        allocate.put(new byte[] {
            0x65,0x6f
        });
    }
}
