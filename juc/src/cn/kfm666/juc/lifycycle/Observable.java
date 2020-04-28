package cn.kfm666.juc.lifycycle;

/**
 * 监控任务的生命周期
 */
public interface Observable {
    /**
     * 任务的生命周期定义
     */
    enum Cycle{
        STARTED,RUNNING,DONE,ERROR
    }

    /**
     * 获取当前任务的生命周期状态
     * @return
     */
    Cycle getCycle();

    /**
     * 定义线程启动的方法，主要是为了屏蔽Thread的其他方法
     * 可通过此方法启动线程
      */
    void start();

    /**
     * 定义线程的中断方法，作用与start方法一样，也是为了屏蔽Thread的其他方法
     */
    void interrupt();
}
