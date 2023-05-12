package com.jon.bytecode.handle;

import com.jon.bytecode.type.ClassFile;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 *  read Java class file this class and super class
 * @author Maolin Yu
 */
public class ThisAndSuperClassHandler  implements BaseByteCodeHandle{
    @Override
    public int order() {
        return 4;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        classFile.setThisClass(new U2(codeBuf.get(), codeBuf.get()));
        classFile.setSuperClass(new U2(codeBuf.get(), codeBuf.get()));
    }
}
