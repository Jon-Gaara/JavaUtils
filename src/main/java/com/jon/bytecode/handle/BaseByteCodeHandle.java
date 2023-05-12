package com.jon.bytecode.handle;

import com.jon.bytecode.type.ClassFile;

import java.nio.ByteBuffer;

/**
 * @author Maolin Yu
 */
public interface BaseByteCodeHandle {
    /**
     * 解释器排序值
     * @return
     */
    int order();

    /**
     * 读取
     * @param codeBuf
     * @param classFile
     * @throws Exception
     */
    void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception;
}
