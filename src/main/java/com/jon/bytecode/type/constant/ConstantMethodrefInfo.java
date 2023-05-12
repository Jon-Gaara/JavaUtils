package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.U1;

/**
 * CONSTANT_Methodref_info在结构上与CONSTANT_Fieldref_info一样，因此可通过继承CONSTANT_Fieldref_info类实现其字段的定义和完成解析工作。
 * CONSTANT_Methodref_info结构的各项说明：
 *  ◆class_index指向的常量必须是一个CONSTANT_Class_Info常量，表示当前方法所在的类的类名；
 *  ◆name_and_type_index指向的常量必须是一个CONSTANT_NameAndType_info常量，表示当前方法的名字和方法描述符
 *
 * @author Maolin Yu
 */
public class ConstantMethodrefInfo extends ConstantFieldrefInfo{

    public ConstantMethodrefInfo(U1 tag) {
        super(tag);
    }

    @Override
    public String toString() {
        return "ConstantMethodrefInfo{" +
                "classIndex=" + super.getClassIndex().toHexString() +
                ", nameAndTypeIndex=" + super.getNameAndTypeIndex().toHexString() +
                '}';
    }
}
