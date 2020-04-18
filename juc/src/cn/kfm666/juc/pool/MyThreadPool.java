package cn.kfm666.juc.pool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池实现
 *
 */
public class MyThreadPool extends Thread implements ThreadPool {

    /** 线程池初始task数量 */
    private final int initSize;
    /** 线程池最大任务数量 */
    private final int maxSize;
    /** 线程池核心线程数量 */
    private final int coreSize;
    /** 活跃线程数量 */
    private int activeCount;
    /** 创建线程工厂 */
    private final ThreadFactory threadFactory;
    /** 线程池关闭标志 */
    private volatile boolean isShutdown = false;
    /** 任务队列 */
    private final Queue<ThreadTask> threadQueue = new ArrayDeque<>();
    private final RunnableQueue runnableQueue;
    /**拒绝策略*/
    private final DenyPolicy denyPolicy;
    private final static ThreadFactory defaultThreadFactory = new ThreadFactory.DefaultThreadFactory("default");
    private final static DenyPolicy defaultPilicy = new DenyPolicy.RunnerDenyPolicy();

    private final long keepAliveTime;
    private final TimeUnit timeUnit;
    private final int queueSize;

    public MyThreadPool(int initSize, int maxSize, int coreSize,int queueSize, ThreadFactory threadFactory, DenyPolicy denyPolicy, long keepAliveTime,TimeUnit timeUnit) {
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.coreSize = coreSize;
        this.threadFactory = threadFactory;
        this.denyPolicy = denyPolicy;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.queueSize = queueSize;
        this.runnableQueue = new LinkedRunnableQueue(queueSize,defaultPilicy,this);
        init();
    }

    public MyThreadPool(int initSize, int maxSize, int coreSize, int queueSize) {
        this(initSize,maxSize,coreSize,queueSize,defaultThreadFactory,defaultPilicy,10,TimeUnit.SECONDS);
    }

    public MyThreadPool(int initSize, int queueSize) {
        this(initSize,initSize,initSize,queueSize,defaultThreadFactory,defaultPilicy,10,TimeUnit.SECONDS);
    }

    @Override
    public void execute(Runnable runnable) {
        if(this.isShutDown()){
            throw new IllegalStateException("线程池已关闭，不能提交线程");
        }
        this.runnableQueue.offer(runnable);
    }

    @Override
    public void shutdown() {
        synchronized (this){
            if(isShutdown) return;
            isShutdown = true;
            threadQueue.forEach(threadTask -> {
                threadTask.stop();
                threadTask.currentThread.interrupt();
            });
            this.interrupt();
        }
    }

    @Override
    public int getInitSize() {
        if(isShutdown){
            throw new IllegalStateException();
        }
        return this.initSize;
    }

    @Override
    public int getMaxSize() {
        if(isShutdown){
            throw new IllegalStateException();
        }
        return this.maxSize;
    }

    @Override
    public int getCoreSize() {
        if(isShutdown){
            throw new IllegalStateException();
        }
        return this.coreSize;
    }

    @Override
    public int getQueueSize() {
        if(isShutdown){
            throw new IllegalStateException();
        }
        return this.runnableQueue.size();
    }

    @Override
    public int getActiveCount() {
        if(isShutdown){
            throw new IllegalStateException();
        }
        return this.activeCount;
    }

    @Override
    public boolean isShutDown() {
        return this.isShutdown;
    }

    private void init(){
        start();
        for(int i = 0; i< initSize;i++){
            newThread();
        }
    }

    private void newThread() {
        ThreadTask task = new ThreadTask(runnableQueue);
        Thread thread = this.threadFactory.createThread(task);
        task.setCurrentThread(thread);
        threadQueue.offer(task);
        this.activeCount ++;
        thread.start();
    }

    private void removeThread(){
        ThreadTask threadTask = threadQueue.remove();
        threadTask.stop();
        this.activeCount --;
    }

    /**
     * 自动维护线程数量
     */
    @Override
    public void run() {
        System.out.println("线程池启动："+Thread.currentThread().getName());
        while (!isShutdown && !isInterrupted()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                isShutdown = true;
                break;
            }
            synchronized (this){
                if(isShutdown){
                    break;
                }
                // 当队列中有未处理的任务，且活跃线程数小于核心线程数
                if(runnableQueue.size()>0 && activeCount < coreSize){
                    System.out.println("创建线程到核心线程数，线程池当前线程数：" + activeCount+"，任务队列数：" + runnableQueue.size());
                    for(int i = initSize;i<coreSize;i++){
                        newThread();
                    }
                    // 不让线程数量直接达到maxSize
                    continue;
                }
                // 如果队列中有任务，且活跃线程数小于最大线程数，则需要更多的线程来处理任务
                if(runnableQueue.size()>0 && activeCount < maxSize){
                    System.out.println("创建线程到最大线程数，线程池当前线程数："+activeCount+"，任务队列数：" + runnableQueue.size());
                    for(int i = coreSize; i< maxSize; i++){
                        newThread();
                    }
                }
                // 如果队列为空，并且最大活跃线程数大于核心线程数，则回收线程到coreSize个
                if(runnableQueue.size() == 0 && activeCount > coreSize){
                    System.out.println("线程池任务队列为空，当前活跃线程数："+activeCount+"，任务队列数：" + runnableQueue.size());
                    for(int i = coreSize; i< activeCount; i++){
                        removeThread();
                    }
                }
            }
        }
    }

    private class ThreadTask implements Runnable{

        private final RunnableQueue runnableQueue;
        private volatile boolean running = true;
        // 任务状态，在0和2的时候任务已经执行结束可以中断
        // 0 等待从队列中获取任务
        // 1 取到了任务将要执行
        // 2 任务执行技术
        private volatile int state = 0;
        public Thread currentThread;

        public void setCurrentThread(Thread currentThread) {
            this.currentThread = currentThread;
        }

        public ThreadTask(RunnableQueue runnableQueue) {
            this.runnableQueue = runnableQueue;
        }

        @Override
        public void run() {
            while (running && !Thread.currentThread().isInterrupted()){
                try {
                    state = 0;
                    Runnable task = runnableQueue.take();
                    state = 1;
                    task.run();
                    state = 2;
                }catch (Exception e){
                    running = false;
                    break;
                }
            }
        }

        public void stop(){
            this.running = false;
            if(state != 1){
                currentThread.interrupt();
            }
        }
    }
}
