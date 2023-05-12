package com.jon.bytecode.handle;

import com.jon.bytecode.type.ClassFile;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 * read Java class file accessFlag
 * @author Maolin Yu
 */
public class AccessFlagHandle implements BaseByteCodeHandle{

    @Override
    public int order() {
        return 3;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        classFile.setAccessFlags(new U2(codeBuf.get(), codeBuf.get()));
    }
}
