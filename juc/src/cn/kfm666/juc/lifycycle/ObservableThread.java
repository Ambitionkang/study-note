package cn.kfm666.juc.lifycycle;

public class ObservableThread<T> extends Thread implements Observable {

    private final TaskLifecycle<T> lifecycle;

    private final Task<T> task;

    private Cycle cycle;

    public ObservableThread(TaskLifecycle<T> lifecycle,Task<T> task){
        super();
        if(task == null){
            throw new IllegalArgumentException("The task is required.");
        }
        this.lifecycle = lifecycle;
        this.task = task;
    }

    @Override
    public void run() {
        this.update(Cycle.STARTED,null,null);
        try {
            this.update(Cycle.RUNNING,null,null);
            T result = this.task.call();
            this.update(Cycle.DONE,result,null);
        }catch (Exception e){
            this.update(Cycle.ERROR,null,e);
        }
    }

    @Override
    public Cycle getCycle() {
        return this.cycle;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    private void update(Cycle cycle, T result, Throwable throwable){
        this.cycle = cycle;
        if(lifecycle == null){
            return;
        }
        try {
            switch (cycle){
                case DONE:
                    this.lifecycle.onFinish(currentThread(),result);
                    break;
                case RUNNING:
                    this.lifecycle.onRunning(currentThread());
                    break;
                case STARTED:
                    this.lifecycle.onStart(currentThread());
                    break;
                case ERROR:
                    this.lifecycle.onError(currentThread(),throwable);
                    break;
            }
        }catch (Exception e){
            if(cycle == Cycle.ERROR){
                throw e;
            }
        }

    }
}
