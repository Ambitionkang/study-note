package cn.kfm666.juc.lock;

/**
 * 尝试用wait/notify实现三个线程按顺序输出abc10次
 */
public class PrintThread implements Runnable {
    private Object prev;
    private Object self;
    private String str;

    public PrintThread(Object prev, Object self, String str) {
        this.prev = prev;
        this.self = self;
        this.str = str;
    }

    @Override
    public void run() {
        int i=0;
        while (i<10){
            synchronized (prev){
                synchronized (self){
                    System.out.println(str);
                    ++i;
                    self.notifyAll();
                }
                if(i< 10){
                    try {
                        prev.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    prev.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        new Thread(new PrintThread(c,a,"A")).start();
        new Thread(new PrintThread(a,b,"B")).start();
        new Thread(new PrintThread(b,c,"C")).start();
    }
}
