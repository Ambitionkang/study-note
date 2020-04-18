package cn.kfm666.juc.lock;

import java.util.Set;

public interface Lock {
    /**
     * 加锁
     * @throws InterruptedException
     */
    void lock() throws InterruptedException;

    /**
     * 尝试加锁
     * 使用wait(timeout)方法实现超时
     * @param mils
     * @return
     * @throws InterruptedException
     */
    boolean tryLock(long mils) throws InterruptedException;

    /**
     * 解锁
     */
    void unlock();

    /**
     * 拿到阻塞队列中的线程集合
     * @return
     */
    Set<Thread> getBlockedThread();
}
