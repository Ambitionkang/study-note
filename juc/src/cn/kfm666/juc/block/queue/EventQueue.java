package cn.kfm666.juc.block.queue;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 利用reentrantLock实现的阻塞队列
 * 用了Condition的await和signal实现阻塞和唤醒
 * @param <T>
 */
public class EventQueue<T> {
    private final int max;
    private LinkedList<T> list = new LinkedList<>();
    private ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    public EventQueue(int max){
        lock = new ReentrantLock();
        this.max = max;
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    public void offer(T t){
        lock.lock();
        try {
            if (list.size() >= max) {
                try {
                    System.out.println("生产者等待");
                    notFull.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.addFirst(t);
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    public T take(){
        lock.lock();
        try {

            if (list.isEmpty()) {
                try {
                    System.out.println("-------------------------------------消费者等待");
                    notEmpty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = list.removeLast();
            notFull.signal();
            return t;
        }finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {

        EventQueue<Long> queue = new EventQueue<>(10);
        new Thread(()->{
            Long i = 0L;
            while (true){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
                queue.offer(i++);
            }
        }).start();
        for(int i=0;i<10;i++){
            new Thread(()->{
                while (true){
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    System.out.println(Thread.currentThread().getName()+"-----------"+queue.take());
                }

            },"thread-"+i).start();
        }
    }

}
