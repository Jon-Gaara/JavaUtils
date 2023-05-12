package com.jon.bytecode.handle;

import com.jon.bytecode.type.ClassFile;
import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U2;
import com.jon.bytecode.type.constant.ConstantLongInfo;

import java.nio.ByteBuffer;

/**
 * read Java class file constantPool
 * @author Maolin Yu
 */
public class ConstantPoolHandle implements BaseByteCodeHandle{

    @Override
    public int order() {
        return 2;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        //获取常量池计数器的值
        U2 cpLength = new U2(codeBuf.get(), codeBuf.get());
        classFile.setConstantPoolCount(cpLength);
        //常量池中常量的总数
        int cpInfoLength = cpLength.toInt() - 1;
        classFile.setConstantPool(new CpInfo[cpInfoLength]);
        //解析所有常量
        for(int i=0;i<cpInfoLength;i++){
            U1 tag = new U1(codeBuf.get());
            CpInfo cpInfo = CpInfo.newCpInfo(tag);
            cpInfo.read(codeBuf);
            classFile.getConstantPool()[i] = cpInfo;
            if (cpInfo instanceof ConstantLongInfo) {
                i++; // jump n+2
            }
        }
    }
}
