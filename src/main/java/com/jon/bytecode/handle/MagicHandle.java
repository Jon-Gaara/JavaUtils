package com.jon.bytecode.handle;

import com.jon.bytecode.type.ClassFile;
import com.jon.bytecode.type.U4;

import java.nio.ByteBuffer;

/**
 * read Java class file magic
 * @author Maolin Yu
 */
public class MagicHandle implements BaseByteCodeHandle{

    private final static String JAVA_MAGIC = "0xCAFEBABE";
    @Override
    public int order() {
        return 0;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        classFile.setMagic(new U4(codeBuf.get(),codeBuf.get(), codeBuf.get(), codeBuf.get()));
        if(!JAVA_MAGIC.equalsIgnoreCase(classFile.getMagic().toHexString())){
            throw new RuntimeException("this is not a java class file");
        }
    }
}
