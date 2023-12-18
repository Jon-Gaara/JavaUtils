package com.jon.bytecode.asm.proxy;

import com.jon.bytecode.asm.util.ByteCodeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 参考: https://github.com/wujiuye/bytecode-book/blob/master/asm-bytecode-project/src/main/java/com/wujiuye/asmbytecode/book/sixth/jdk/MyProxy.java
 * @author Maolin Yu
 */
public class MyProxy {
    protected InvocationHandler h;

    private final static AtomicInteger PROXY_CNT = new AtomicInteger(0);

    private MyProxy(){

    }

    protected MyProxy(InvocationHandler h){
        this.h = h;
    }

    public static Object newProxyInstance(Object proxy,Class<?>[] interfaces,InvocationHandler h) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String proxyClassName = "com/sun/proxy/$Proxy"+PROXY_CNT.getAndIncrement();
        //创建代理类
        byte[] proxyClassBytes = MyProxyFactory.createProxyClass(proxyClassName,interfaces);
        //加载代理类
        Class<?> proxyClass = ByteCodeUtils.loadClass(proxyClassName,proxyClassBytes);
        Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
        return constructor.newInstance(h);
    }
}
