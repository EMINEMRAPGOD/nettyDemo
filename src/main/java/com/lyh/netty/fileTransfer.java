package com.lyh.netty;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2021/12/19
 * @Description:
 */
public class fileTransfer {
    public static void main(String[] args) {
        try(FileChannel from = new FileInputStream("data.txt").getChannel();
            FileChannel to = new FileOutputStream("to.txt").getChannel();
        ){
            from.transferTo(0,from.size(),to);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
