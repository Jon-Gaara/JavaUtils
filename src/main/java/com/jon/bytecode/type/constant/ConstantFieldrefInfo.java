package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;

/**
 * 根据《Java虚拟机规范》规定，CONSTANT_Fieldref_info常量存储字段的符号信息，除tag字段外，有两个U2类型的指向常量池中某个常量的索引字段，分别是class_index、name_and_type_index。
 * CONSTANT_Fieldref_info结构的各项说明：
 *  ◆class_index指向的常量必须是一个CONSTANT_Class_Info常量，表示当前字段所在的类的类名；
 *  ◆name_and_type_index指向的常量必须是一个CONSTANT_NameAndType_info常量，表示当前字段的名字和类型描述符
 * @author Maolin Yu
 */
public class ConstantFieldrefInfo  extends CpInfo {

    private U2 classIndex;
    private U2 nameAndTypeIndex;

    public ConstantFieldrefInfo(U1 tag){
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        classIndex = new U2(codeBuf.get(), codeBuf.get());
        nameAndTypeIndex = new U2(codeBuf.get(), codeBuf.get());
    }

    public U2 getClassIndex() {
        return classIndex;
    }

    public U2 getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    @Override
    public String toString() {
        return "ConstantFieldrefInfo{" +
                "classIndex=" + classIndex.toHexString() +
                ", nameAndTypeIndex=" + nameAndTypeIndex.toHexString() +
                '}';
    }
}
