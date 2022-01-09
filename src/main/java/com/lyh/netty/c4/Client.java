package com.lyh.netty.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2021/12/19
 * @Description:
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel  sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8080));
        sc.write(Charset.defaultCharset().encode("123456789abcdef\n"));
        System.in.read();
        System.out.println("waiting...");
    }
}
