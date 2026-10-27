package com.jon.bytecode.asm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

import com.jon.bytecode.asm.proxy.MyProxyFactory;

public class AsmProxyTest {

    public interface IntBox {
        int value();
    }

    @Test
    public void generatedClassIsVerifiable() throws Exception {
        GenerateClass.generateClass(true);
        // 修复前 main 方法没有 RETURN/visitMaxs，类无法通过 JVM 校验
        try (URLClassLoader loader = new URLClassLoader(new URL[] {new File(".").toURI().toURL()},
            getClass().getClassLoader())) {
            Class<?> clazz = Class.forName("com.jon.bytecode.asm.AsmClass", true, loader);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } finally {
            // 清理生成物，避免污染工作区
            new File("com/jon/bytecode/asm/AsmClass.class").delete();
        }
    }

    @Test
    public void proxyIntReturnMethodWorks() throws Exception {
        // 修复前使用 INVOKESPECIAL 调用 Integer.intValue，触发 VerifyError
        byte[] proxyBytes = MyProxyFactory.createProxyClass("TestProxy", new Class<?>[] {IntBox.class});
        Class<?> proxyClass = new ClassLoader(getClass().getClassLoader()) {
            Class<?> define() {
                return defineClass("TestProxy", proxyBytes, 0, proxyBytes.length);
            }
        }.define();

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                return 42;
            }
        };
        Object proxy = proxyClass.getConstructor(InvocationHandler.class).newInstance(handler);
        assertEquals(42, ((IntBox)proxy).value());
    }
}
