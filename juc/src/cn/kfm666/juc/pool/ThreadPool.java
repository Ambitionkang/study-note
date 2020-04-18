package cn.kfm666.juc.pool;

/**
 * 自定义线程池接口
 */
public interface ThreadPool {
    /**
     * 执行
     * @param runnable
     */
    void execute(Runnable runnable);

    /**
     * 停止线程池
     */
    void shutdown();

    /**
     * 获取线程池初始线程数量
     * @return
     */
    int getInitSize();

    /**
     * 获取线程池最大线程数量
     * @return
     */
    int getMaxSize();

    /**
     * 获取线程池核心线程数量
     * @return
     */
    int getCoreSize();

    /**
     * 获取线程池缓存任务队列的容量
     * @return
     */
    int getQueueSize();

    /**
     * 获取线程池中活跃线程数量
     * @return
     */
    int getActiveCount();

    /**
     * 查看线程池是否被shutdown
     * @return
     */
    boolean isShutDown();
}
