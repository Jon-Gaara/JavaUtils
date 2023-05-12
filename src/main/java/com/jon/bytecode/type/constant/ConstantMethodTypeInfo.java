package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 * CONSTANT_MethodType_info结构表示方法类型，与CONSTANT_MethodHandle_info结构一样，也是虚拟机为实现动态调用invokedynamic指令所增加的常量结构。
 * CONSTANT_MethodType_info除tag字段外，只有一个u2类型的描述符指针字段descriptor_index，指向常量池中的某一CONSTANT_Utf8_info结构的常量
 * @author Maolin Yu
 */
public class ConstantMethodTypeInfo extends CpInfo {

    private U2 descriptorIndex;

    public ConstantMethodTypeInfo(U1 tag) {
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        descriptorIndex = new U2(codeBuf.get(), codeBuf.get());
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }

    @Override
    public String toString() {
        return "ConstantMethodTypeInfo{" +
                "descriptorIndex=" + descriptorIndex.toHexString() +
                '}';
    }
}
