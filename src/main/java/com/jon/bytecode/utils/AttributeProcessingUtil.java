package com.jon.bytecode.utils;

import com.jon.bytecode.type.AttributeInfo;
import com.jon.bytecode.type.CodeAttribute;
import com.jon.bytecode.type.U2;
import com.jon.bytecode.type.U4;
import com.jon.bytecode.type.constant.ConstantValueInfo;
import com.sun.org.apache.bcel.internal.classfile.Code;

import java.nio.ByteBuffer;

/**
 * @author Maolin Yu
 */
public class AttributeProcessingUtil {

    public static ConstantValueInfo constantValue(AttributeInfo attributeInfo){
        ConstantValueInfo constantValueInfo = new ConstantValueInfo();
        constantValueInfo.setAttributeNameIndex(attributeInfo.getAttributeNameIndex());
        constantValueInfo.setAttributeLength(attributeInfo.getAttributeLength());
        constantValueInfo.setConstantValueIndex(new U2(attributeInfo.getInfo()[0], attributeInfo.getInfo()[1]));
        return constantValueInfo;
    }

    public static CodeAttribute code(AttributeInfo attributeInfo){
        CodeAttribute attribute = new CodeAttribute();
        ByteBuffer body = ByteBuffer.wrap(attributeInfo.getInfo());
        attribute.setMaxStack(new U2(body.get(), body.get()));
        attribute.setMaxLocals(new U2(body.get(), body.get()));
        attribute.setCodeLength(new U4(body.get(), body.get(), body.get(), body.get()));
        byte[] byteCode = new byte[attribute.getCodeLength().toInt()];
        body.get(byteCode,0,byteCode.length);
        attribute.setCode(byteCode);
        return attribute;
    }
}
