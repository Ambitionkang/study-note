package cn.kfm666.juc.pool;

import java.util.LinkedList;

/**
 * 用LinkedList实现的队列
 */
public class LinkedRunnableQueue implements RunnableQueue {

    private final int limit;
    private final DenyPolicy denyPolicy;
    private final LinkedList<Runnable> tasks = new LinkedList<>();
    private final ThreadPool threadPool;

    public LinkedRunnableQueue(int limit,DenyPolicy denyPolicy,ThreadPool threadPool){
        this.limit = limit;
        this.denyPolicy = denyPolicy;
        this.threadPool = threadPool;
    }



    @Override
    public void offer(Runnable runnable) {
        synchronized (tasks){
            if(limit <= tasks.size()){
                denyPolicy.reject(runnable,threadPool);
            }else{
                tasks.addLast(runnable);
                tasks.notifyAll();
            }
        }

    }

    @Override
    public Runnable take() {
        synchronized (tasks){
            while (tasks.isEmpty()){
                try {
                    tasks.wait();
                } catch (InterruptedException e) {
                    System.out.println("Thread："+Thread.currentThread().getName()+"取任务时被中断等待");
                }
            }
            return tasks.removeFirst();
        }
    }

    @Override
    public int size() {
        return tasks.size();
    }
}
