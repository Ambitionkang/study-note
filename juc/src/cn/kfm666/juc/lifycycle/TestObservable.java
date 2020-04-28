package cn.kfm666.juc.lifycycle;

public class TestObservable {

    public static void main(String[] args) {
        TaskLifecycle<String> lifecycle = new TaskLifecycle.EmptyLifecycle<String>(){
            @Override
            public void onStart(Thread thread) {
                System.out.println("任务启动了");
            }

            @Override
            public void onFinish(Thread thread, String result) {
                System.out.println("任务结束了，结果："+result);
            }

            @Override
            public void onError(Thread thread, Throwable throwable) {
                System.out.println("任务出错了"+throwable);
            }

            @Override
            public void onRunning(Thread thread) {
                System.out.println("任务运行中");
            }
        };
        ObservableThread<String> thread = new ObservableThread<>(lifecycle,()->{
            Thread.sleep(1000);
            System.out.println(1/0);
            return "我是返回结果";
        });
        thread.start();
    }
}
