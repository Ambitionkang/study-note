package cn.kfm666.juc.pool;

/**
 * 拒绝策略
 * 线程缓存队列满时的处理策略
 * 实现了丢弃、调用者执行、抛出异常等拒绝策略
 * jdk默认实现中还有一个丢弃队列中最老的线程的策略
 */
public interface DenyPolicy {
    /**
     * 拒绝
     * @param runnable
     * @param threadPool
     */
    void reject(Runnable runnable, ThreadPool threadPool);

    class DiscardDenyPolicy implements DenyPolicy{

        /**
         * 直接丢弃任务
         * @param runnable
         * @param threadPool
         */
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            System.out.println("丢弃任务");
        }
    }

    class AbortDenyPolicy implements DenyPolicy{

        /**
         * 向任务提交者抛出异常
         * @param runnable
         * @param threadPool
         */
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            throw new RuntimeException("任务队列已满，拒绝任务");
        }
    }

    class RunnerDenyPolicy implements DenyPolicy{

        /**
         * 由任务提交者线程执行此任务
         * @param runnable
         * @param threadPool
         */
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            if(!threadPool.isShutDown()){
                System.err.println("执行拒绝策略，当前活跃线程数："+threadPool.getActiveCount()
                        +"。任务队列数量："+threadPool.getQueueSize()
                        +"。");
                runnable.run();
            }
        }
    }
}
