package com.lyh.netty.netty.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
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
public class TestNettyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        EventLoop next = eventExecutors.next();
        Future<Integer> fu = next.submit(() -> {
            Thread.sleep(1000);
            return 70;
        });
        log.debug("等待结果");
        log.debug("结果是{}",fu.get());
        //异步
        fu.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                log.debug("接受结果:{}",fu.getNow());//不阻塞 立刻获取结果
            }
        });

    }
}
