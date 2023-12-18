package com.jon.bytecode.asm.util;

import cn.hutool.core.io.FileUtil;

import java.util.HashMap;
import java.util.Map;

public class ByteCodeClassLoader extends ClassLoader{

    private final Map<String, ByteCodeHolder> classes = new HashMap<>();

    public ByteCodeClassLoader(final ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        ByteCodeHolder handler = classes.get(name);
        if (handler != null) {
            byte[] bytes = handler.getByteCode();
            FileUtil.writeBytes(bytes,name+".class");
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }

    public void add(final String name, final ByteCodeHolder handler) {
        classes.put(name.replace("/", "."), handler);
    }

    /**
     * 加载类
     *
     * @param name 全类名
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        name = name.replace("/", ".");
        return super.loadClass(name);
    }
}
