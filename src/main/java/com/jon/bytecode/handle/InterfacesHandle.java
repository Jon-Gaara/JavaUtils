package com.jon.bytecode.handle;

import com.jon.bytecode.type.ClassFile;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 * read Java class file interfaces and interface count
 * @author Maolin Yu
 */
public class InterfacesHandle implements BaseByteCodeHandle{
    @Override
    public int order() {
        return 5;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        classFile.setInterfacesCount(new U2(codeBuf.get(), codeBuf.get()));
        int interfaceCount = classFile.getInterfacesCount().toInt();
        U2[] interfaces = new U2[interfaceCount];
        for(int i=0;i<interfaceCount;i++){
            interfaces[i] = new U2(codeBuf.get(), codeBuf.get());
        }
        classFile.setInterfaces(interfaces);
    }
}
