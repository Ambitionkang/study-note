package cn.kfm666.juc.lock;

import java.util.HashSet;

/**
 * 测试加锁，然后进行i++操作会不会出现问题
 */
public class MyLockTest {
    volatile static Integer num = 0;
    public static void main(String[] args) {
        MyLock lock = new MyLock();
        HashSet<Integer> set = new HashSet<>();
        for(int i=0;i<10;i++){
            new Thread(()->{
                while (num<10000){
                    try {
                        lock.lock();
                        if(num>=10000){
                            break;
                        }
                        set.add(num++);
                        System.out.println(num);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }


                }
            },"thread---------"+i).start();
        }
    }
}
