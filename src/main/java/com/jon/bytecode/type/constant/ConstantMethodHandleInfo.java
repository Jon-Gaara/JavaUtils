package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 * 根据《Java虚拟机规范》规定，CONSTANT_MethodHandle_info结构用于存储方法句柄，这是虚拟机为实现动态调用invokedynamic指令所增加的常量结构。
 * CONSTANT_MethodHandle_info结构除必须的tag字段外，有一个U1类型的字段reference_kind，取值范围为1~9，包括1和9，表示方法句柄的类型，还
 * 有一个U2类型的字段reference_index，其值为指向常量池中某个常量的索引
 * @author Maolin Yu
 */
public class ConstantMethodHandleInfo extends CpInfo {

    private U1 referenceKind;

    private U2 referenceIndex;

    public ConstantMethodHandleInfo(U1 tag) {
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        referenceKind  = new U1(codeBuf.get());
        referenceIndex = new U2(codeBuf.get(), codeBuf.get());
    }

    public U1 getReferenceKind() {
        return referenceKind;
    }

    public U2 getReferenceIndex() {
        return referenceIndex;
    }

    @Override
    public String toString() {
        return "ConstantMethodHandleInfo{" +
                "referenceKind=" + referenceKind.toHexString() +
                ", referenceIndex=" + referenceIndex.toHexString() +
                '}';
    }
}
