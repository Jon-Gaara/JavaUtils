package com.jon.bytecode.handle;

import com.jon.bytecode.type.ClassFile;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 * read Java class file minor version and major version
 * @author Maolin Yu
 */
public class VersionHandle implements BaseByteCodeHandle {
    @Override
    public int order() {
        return 1;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        classFile.setMinorVersion(new U2(codeBuf.get(), codeBuf.get()));
        classFile.setMajorVersion(new U2(codeBuf.get(), codeBuf.get()));
    }
}
