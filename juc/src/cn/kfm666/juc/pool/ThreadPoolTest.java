package cn.kfm666.juc.pool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTest {
    public static void main(String[] args) {
        AtomicInteger integer = new AtomicInteger(0);
        final ThreadPool threadPool = new MyThreadPool(3,10,5,5);
        for(int i = 0;i < 20;i++){
            threadPool.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("线程"+Thread.currentThread().getName()+"执行完毕"+integer.getAndAdd(1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
