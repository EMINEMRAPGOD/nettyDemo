package com.lyh.netty.netty.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2022/01/09
 * @Description:
 */
@Slf4j
public class TestPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.准备eventloop对象
        EventLoop eventLoop = new NioEventLoopGroup().next();
        //2.可以主动创建promise方法,结果容器，
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        //任意线程执行计算，计算完成后向promise填充结果,也可以填充异常
        new Thread(()->{

            log.debug("开始装数据");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            promise.setSuccess(80);
        }).start();

        //接受结果的线程
        log.debug("等待结果");
        log.debug("结果是:{}",promise.get());
    }
}
