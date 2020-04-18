package cn.kfm666.juc.lock;

/**
 * 尝试用wait/notify实现线程的通信
 * 两个线程轮流输出a/b
 */
public class TestWaitNotify {
    static Object object = new Object();
    public static void main(String[] args) {
        new Thread(()->{
            while (true){
                synchronized (object){
                    System.out.println("A");
                    try {
                        object.notifyAll();
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
        new Thread(()->{
            while (true){
                synchronized (object){
                    System.out.println("B");
                    try {
                        object.notifyAll();
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();

    }
}
