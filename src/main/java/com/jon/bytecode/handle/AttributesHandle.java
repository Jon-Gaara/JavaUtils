package com.jon.bytecode.handle;

import com.jon.bytecode.type.AttributeInfo;
import com.jon.bytecode.type.ClassFile;
import com.jon.bytecode.type.U2;
import com.jon.bytecode.type.U4;

import java.nio.ByteBuffer;

/**
 * @author Maolin Yu
 */
public class AttributesHandle implements BaseByteCodeHandle{
    @Override
    public int order() {
        return 8;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        classFile.setAttributesCount(new U2(codeBuf.get(), codeBuf.get()));
        int attrCount = classFile.getAttributesCount().toInt();
        if(attrCount == 0){
            return;
        }
        AttributeInfo[] attributeInfos = new AttributeInfo[attrCount];
        classFile.setAttributes(attributeInfos);
        for(int i=0;i<attrCount;i++){
            AttributeInfo attributeInfo = new AttributeInfo();
            attributeInfos[i] = attributeInfo;
            attributeInfo.setAttributeNameIndex(new U2(codeBuf.get(), codeBuf.get()));
            attributeInfo.setAttributeLength(new U4(codeBuf.get(), codeBuf.get(), codeBuf.get(), codeBuf.get()));
            int attrLength = attributeInfo.getAttributeLength().toInt();
            if(attrLength == 0){
                continue;
            }
            byte[] info = new byte[attrLength];
            codeBuf.get(info,0,attrLength);
            attributeInfo.setInfo(info);
        }
    }
}
