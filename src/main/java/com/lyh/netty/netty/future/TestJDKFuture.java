package com.lyh.netty.netty.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created with IDEA
 *
 * @Author : lyh
 * @Date : 2022/01/09
 * @Description:
 */
@Slf4j
public class TestJDKFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //2.提交任务
        Future<Integer> future = executorService.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                log.debug("执行计算");
                Thread.sleep(1000);
                return 50;
            }
        });
        //3.主线程通过future获取结果
        log.debug("等待结果");
        Integer integer = future.get();
        System.out.println(integer);

    }
}
