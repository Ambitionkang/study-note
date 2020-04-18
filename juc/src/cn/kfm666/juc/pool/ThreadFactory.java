package cn.kfm666.juc.pool;

import java.util.concurrent.atomic.AtomicInteger;

public interface ThreadFactory {

    /**
     * 创建线程
     * @param runnable
     * @return
     */
    Thread createThread(Runnable runnable);

    class DefaultThreadFactory implements ThreadFactory{

        private AtomicInteger count = new AtomicInteger(0);
        private ThreadGroup group;

        public DefaultThreadFactory(String groupName) {
            this.group = new ThreadGroup(groupName);
        }

        @Override
        public Thread createThread(Runnable runnable) {
            return new Thread(group,runnable,group.getName()+"---"+count.getAndAdd(1));
        }
    }
}
