package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 * 根据《Java虚拟机规范》规定，CONSTANT_NameAndType_info结构用于存储字段的名称和字段的类型描述符，或者是用于存储方法的名称和方法的描述符
 * CONSTANT_NameAndType_info结构除tag字段外，还有一个U2类型的字段name_index和一个U2类型的字段descriptor_index，分别对应名称指向常量池中某个常量的索引和描述符指向常量池中某个常量的索引，这两个字段指向的常量都必须是CONSTANT_Utf8_info结构的常量
 *
 * @author Maolin Yu
 */
public class ConstantNameAndTypeInfo extends CpInfo {

    private U2 nameIndex;

    private U2 descriptorIndex;

    public ConstantNameAndTypeInfo(U1 tag) {
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        //名称索引
        nameIndex = new U2(codeBuf.get(), codeBuf.get());
        //描述符索引
        descriptorIndex = new U2(codeBuf.get(), codeBuf.get());
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }

    @Override
    public String toString() {
        return "ConstantNameAndTypeInfo{" +
                "nameIndex=" + nameIndex.toHexString() +
                ", descriptorIndex=" + descriptorIndex.toHexString() +
                '}';
    }
}
