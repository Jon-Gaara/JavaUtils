package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 * 根据《Java虚拟机规范》规定，CONSTANT_Class_Info常量存储类的符号信息，
 * 除tag字段外，只有一个存储指向常量池表中某一常量的索引字段name_index，name_index指向的常量必须是一个CONSTANT_Utf8_info常量，
 * 该常量存储class的类名（内部类名[1]）
 * @author Maolin Yu
 */
public class ConstantClassInfo extends CpInfo {

    private U2 nameIndex;

    public ConstantClassInfo(U1 tag){
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        nameIndex = new U2(codeBuf.get(), codeBuf.get());
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    @Override
    public String toString() {
        return "ConstantClassInfo{" +
                "nameIndex=" + nameIndex.toHexString()+
                '}';
    }
}
