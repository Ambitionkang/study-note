package cn.kfm666.juc.pool;

/**
 * 线程缓存队列
 */
public interface RunnableQueue {
    /**
     * 添加线程到队列中
     * @param runnable
     */
    void offer(Runnable runnable);

    /**
     * 取出线程
     * @return
     */
    Runnable take();

    /**
     * 返回队列中任务的数量
     * @return
     */
    int size();
}
