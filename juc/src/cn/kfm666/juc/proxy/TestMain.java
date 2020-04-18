package cn.kfm666.juc.proxy;

import java.lang.reflect.Proxy;

public class TestMain {
    public static void main(String[] args) {
        TestService service = new TestServiceImpl();
        MyHandler handler = new MyHandler(service);
        TestService proxy = (TestService) Proxy
                .newProxyInstance(service.getClass().getClassLoader(),new Class[]{TestService.class},handler);
        proxy.test();
    }
}
