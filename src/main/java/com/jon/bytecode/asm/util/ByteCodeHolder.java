package com.jon.bytecode.asm.util;

public interface ByteCodeHolder {
    /**
     * 获取类名
     *
     * @return
     */
    String getClassName();

    /**
     * 获取类的字节码
     *
     * @return
     */
    byte[] getByteCode();
}
