package com.jon.bytecode.handle;

import java.nio.ByteBuffer;

/**
 * @author Maolin Yu
 */
public interface ConstantInfoHandle {
    /**
     * read
     * @param codeBuf
     * @throws Exception
     */
    void read(ByteBuffer codeBuf) throws Exception;
}
