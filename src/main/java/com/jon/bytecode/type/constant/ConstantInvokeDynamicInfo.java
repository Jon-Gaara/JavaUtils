package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 * CONSTANT_InvokeDynamic_info表示invokedynamic指令用到的引导方法bootstrap method以及引导方法所用到的动态调用名称、参数、返回类型。
 * CONSTANT_InvokeDynamic_info结构除tag字段外，有两个U2类型的字段，分别是bootstrap_method_attr_index和name_and_type_index，
 * 前者指向class文件结构属性表中引导方法表的某个引导方法，后者指向常量池中某个CONSTANT_NameAndType_Info结构的常量。
 * @author Maolin Yu
 */
public class ConstantInvokeDynamicInfo extends CpInfo {

    private U2 bootstrapMethodAttrIndex;

    private U2 nameAndTypeIndex;

    public ConstantInvokeDynamicInfo(U1 tag) {
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        bootstrapMethodAttrIndex = new U2(codeBuf.get(), codeBuf.get());
        nameAndTypeIndex = new U2(codeBuf.get(), codeBuf.get());
    }

    public U2 getBootstrapMethodAttrIndex() {
        return bootstrapMethodAttrIndex;
    }

    public U2 getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    @Override
    public String toString() {
        return "ConstantInvokeDynamicInfo{" +
                "bootstrapMethodAttrIndex=" + bootstrapMethodAttrIndex.toHexString() +
                ", nameAndTypeIndex=" + nameAndTypeIndex.toHexString() +
                '}';
    }
}
