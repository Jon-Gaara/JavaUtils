package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 * 根据《Java虚拟机规范》规定，CONSTANT_String_info结构存储Java中String类型的常量，
 * 除tag字段外，还有一个U2类型的字段string_index，值为常量池中某个常量的索引，该索引指向的常量必须是一个CONSTANT_Utf8_info常量。
 *
 * @author Maolin Yu
 */
public class ConstantStringInfo extends CpInfo {

    private U2 stringIndex;

    public ConstantStringInfo(U1 tag) {
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        stringIndex = new U2(codeBuf.get(),codeBuf.get());
    }

    public U2 getStringIndex() {
        return stringIndex;
    }

    @Override
    public String toString() {
        return "ConstantStringInfo{" +
                "stringIndex=" + stringIndex.toHexString() +
                '}';
    }
}
