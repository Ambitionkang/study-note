package cn.kfm666.juc.lifycycle;

public interface Task<T> {
    /**
     * 任务执行接口，该接口允许有返回值
     * @return
     */
    T call() throws InterruptedException;
}
