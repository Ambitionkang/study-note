package cn.kfm666.juc.lifycycle;

/**
 * 任务生命周期接口
 */
public interface TaskLifecycle<T> {
    /**
     * 任务启动时会触发onStart方法
     * @param thread
     */
    void onStart(Thread thread);

    /**
     * 任务正在运行时会触发onRunning方法
     * @param thread
     */
    void onRunning(Thread thread);

    /**
     * 任务运行结束时会触发onFinish方法
     * @param thread
     * @param result
     */
    void onFinish(Thread thread,T result);

    /**
     * 任务执行报错时会触发onError方法
     * @param thread
     * @param throwable
     */
    void onError(Thread thread,Throwable throwable);

    /**
     * 一个空的生命周期方法实现，主要是为了让使用者保持对Thread的使用习惯
     * 使用者可按需继承此空实现类并覆盖任意方法
     */
    class EmptyLifecycle<T> implements TaskLifecycle<T>{

        @Override
        public void onStart(Thread thread) {

        }

        @Override
        public void onRunning(Thread thread) {

        }

        @Override
        public void onFinish(Thread thread, T result) {

        }

        @Override
        public void onError(Thread thread, Throwable throwable) {

        }
    }
}
