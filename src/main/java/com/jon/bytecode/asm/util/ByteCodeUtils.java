package com.jon.bytecode.asm.util;

public class ByteCodeUtils {

    private final static ByteCodeClassLoader byteCodeClassLoader
            = new ByteCodeClassLoader(ByteCodeUtils.class.getClassLoader());

    /**
     * 加载类
     *
     * @param className 类名
     * @param byteCode  字节数组
     * @return
     * @throws ClassNotFoundException
     */
    public static Class loadClass(final String className, final byte[] byteCode) throws ClassNotFoundException {
        ByteCodeHolder holder = new ByteCodeHolder() {
            @Override
            public String getClassName() {
                return className;
            }

            @Override
            public byte[] getByteCode() {
                return byteCode;
            }
        };
        byteCodeClassLoader.add(className,holder);
        return byteCodeClassLoader.loadClass(className);
    }
}
