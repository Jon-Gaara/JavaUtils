package com.jon.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TraceHandle implements InvocationHandler {

    private Object target;

    public TraceHandle(Object values) {
        this.target = values;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.print(target);
        System.out.print("." + method.getName() + "(");
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                System.out.print(args[i]);
                if (i < args.length - 1)
                    System.out.print(", ");
            }
        }
        System.out.println(")");
        return method.invoke(target, args);
    }
}
