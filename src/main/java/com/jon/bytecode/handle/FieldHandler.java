package com.jon.bytecode.handle;

import com.jon.bytecode.type.*;

import java.nio.ByteBuffer;

/**
 * read Java class fiel Field
 * @author Maolin Yu
 */
public class FieldHandler implements BaseByteCodeHandle{
    @Override
    public int order() {
        return 6;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        classFile.setFieldsCount(new U2(codeBuf.get(), codeBuf.get()));
        int fieldCount = classFile.getFieldsCount().toInt();
        if(fieldCount == 0){
            return;
        }
        FieldInfo[] fieldInfos = new FieldInfo[fieldCount];
        classFile.setFields(fieldInfos);
        for(int i=0;i<fieldInfos.length;i++){
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfos[i] = fieldInfo;
            fieldInfo.setAccessFlag(new U2(codeBuf.get(), codeBuf.get()));
            fieldInfo.setNameIndex(new U2(codeBuf.get(), codeBuf.get()));
            fieldInfo.setDescriptorIndex(new U2(codeBuf.get(), codeBuf.get()));
            fieldInfo.setAttributesCount(new U2(codeBuf.get(), codeBuf.get()));
            int attrLength = fieldInfo.getAttributesCount().toInt();
            if(attrLength == 0){
                continue;
            }
            fieldInfo.setAttributes(new AttributeInfo[attrLength]);
            for(int j=0;j<attrLength;j++){
                AttributeInfo attributeInfo = new AttributeInfo();
                fieldInfo.getAttributes()[j].setAttributeNameIndex(new U2(codeBuf.get(), codeBuf.get()));
                U4 attrInfoLength = new U4(codeBuf.get(), codeBuf.get(), codeBuf.get(), codeBuf.get());
                fieldInfo.getAttributes()[j].setAttributeLength(attrInfoLength);
                byte[] info = new byte[attrInfoLength.toInt()];
                codeBuf.get(info,0,attrInfoLength.toInt());
                attributeInfo.setInfo(info);
                fieldInfo.getAttributes()[j] = attributeInfo;
            }
        }
    }
}
