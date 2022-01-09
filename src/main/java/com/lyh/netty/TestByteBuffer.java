package com.lyh.netty;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2021/12/18
 * @Description:
 */
@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) throws FileNotFoundException {
//        InputStream resourceAsStream = TestByteBuffer.class.getClassLoader().getResourceAsStream("/src/main/java/com/lyh/netty/data.txt");
        FileInputStream fileInputStream = new FileInputStream("src\\main\\java\\com\\lyh\\netty\\data.txt");
        try(FileChannel channel = fileInputStream.getChannel()){

            //准备缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            while(true){
                int len = channel.read(byteBuffer);
                log.debug("读取到的字节数{}",len);
                if(len==-1) {
                    break;
                }
                //切换到读模式
                byteBuffer.flip();

                while(byteBuffer.hasRemaining()){
                    byte b = byteBuffer.get();
                    log.debug("实际字节:{}",(char)b);
                }
               byteBuffer.clear();
            }
        }catch (IOException e) {
        }
    }
}
