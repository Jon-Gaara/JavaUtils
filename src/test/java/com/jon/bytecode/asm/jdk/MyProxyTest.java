package com.jon.bytecode.asm.jdk;

import com.jon.bytecode.asm.proxy.MyProxy;

import java.lang.reflect.InvocationTargetException;

public class MyProxyTest {

    static {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
    }

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HttpRequestTemplate target = new HttpRequestTemplateImpl();
        HttpRequestTemplate requestTemplate = (HttpRequestTemplate) MyProxy.newProxyInstance(null,new Class[]{HttpRequestTemplate.class},new HttpRequestInvocationHandler(target));
        HttpRequest request = new HttpRequest();
        HttpResponse response = requestTemplate.doGet(request);
        System.out.println(response);
    }
}
