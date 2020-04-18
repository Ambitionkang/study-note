package cn.kfm666.juc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyHandler implements InvocationHandler {

    private TestService target;

    public MyHandler(TestService target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object obj = method.invoke(target,args);
        System.out.println("after");
        return obj;
    }
}
