package com.lyh.netty;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2021/12/19
 * @Description:
 */
public class TestFilesWorkTree {
    public static void main(String[] args) throws IOException {
        AtomicInteger counter = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("C:\\Users\\34266\\AppData\\Roaming\\Typora\\conf"), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("====>"+dir);
                counter.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return super.postVisitDirectory(dir, exc);
            }
        });
        System.out.println(fileCount+" "+counter);

        //拷贝多级目录
        String source = "a";
        String target = "b";

        Files.walk(Paths.get(source)).forEach((path -> {
            try {
                String targetName = path.toString().replace(source,target);
                if(Files.isDirectory(path)){
                    Files.createDirectory(Paths.get(targetName));
                }else if(Files.isRegularFile(path)){
                    Files.copy(path,Paths.get(targetName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}
