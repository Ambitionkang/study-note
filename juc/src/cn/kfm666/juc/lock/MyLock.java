package cn.kfm666.juc.lock;

import java.util.HashSet;
import java.util.Set;

/**
 * 使用wait和notify实现的锁
 */
public class  MyLock implements Lock {
    private HashSet<Thread> waitSet = new HashSet<>();
    // true表示锁已被某个线程获得，false表示没线程持有这个锁
    private boolean state = false;
    private Thread currentThread;
    @Override
    public void lock() throws InterruptedException {
        synchronized (this){
            while (state){
                this.wait();
            }
            state = true;
            currentThread = Thread.currentThread();
        }
    }

    @Override
    public boolean tryLock(long mils) throws InterruptedException {
        synchronized (this){
            long remain = mils;
            long currentTime = System.currentTimeMillis();
            while (state){
                if(currentThread == Thread.currentThread())  {
                    return true;
                }
                if(remain > 0){
                    waitSet.add(Thread.currentThread());
                    long t = System.currentTimeMillis();
                    this.wait(remain);
                }

                remain = remain - (System.currentTimeMillis() - currentTime);
                if(remain<0){
                    waitSet.remove(Thread.currentThread());
                    return false;
                }
            }
            state = true;
            currentThread = Thread.currentThread();
            return true;
        }
    }

    @Override
    public void unlock() {
        synchronized (this){
            if(currentThread != Thread.currentThread()){
                throw new IllegalMonitorStateException("当前线程没有持有"+this+"锁");
            }
            currentThread = null;
            state = false;
            this.notifyAll();
        }
    }

    @Override
    public Set<Thread> getBlockedThread() {
        return waitSet;
    }
}
