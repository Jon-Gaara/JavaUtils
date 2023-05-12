package com.jon.bytecode.handle;

import com.jon.bytecode.type.*;

import java.nio.ByteBuffer;

/**
 * @author Maolin Yu
 */
public class MethodHandle implements BaseByteCodeHandle{
    @Override
    public int order() {
        return 7;
    }

    @Override
    public void read(ByteBuffer codeBuf, ClassFile classFile) throws Exception {
        classFile.setMethodsCount(new U2(codeBuf.get(), codeBuf.get()));
        int methodCount = classFile.getMethodsCount().toInt();
        if(methodCount == 0){
            return;
        }
        MethodInfo[] methodInfos = new MethodInfo[methodCount];
        classFile.setMethods(methodInfos);
        for(int i=0;i<methodCount;i++){
            MethodInfo methodInfo = new MethodInfo();
            methodInfos[i] = methodInfo;
            methodInfo.setAccessFlags(new U2(codeBuf.get(), codeBuf.get()));
            methodInfo.setNameIndex(new U2(codeBuf.get(), codeBuf.get()));
            methodInfo.setDescriptorIndex(new U2(codeBuf.get(), codeBuf.get()));
            methodInfo.setAttributesCount(new U2(codeBuf.get(), codeBuf.get()));
            int attributeCount = methodInfo.getAttributesCount().toInt();
            if(attributeCount == 0){
                continue;
            }
            methodInfo.setAttributeInfos(new AttributeInfo[attributeCount]);
            for(int j=0;j<attributeCount;j++){
                AttributeInfo attributeInfo = new AttributeInfo();
                methodInfo.getAttributeInfos()[j] = attributeInfo;
                attributeInfo.setAttributeNameIndex(new U2(codeBuf.get(), codeBuf.get()));
                U4 attributeInfoLengthByte = new U4(codeBuf.get(), codeBuf.get(), codeBuf.get(), codeBuf.get());
                attributeInfo.setAttributeLength(attributeInfoLengthByte);
                int attributeInfoLength = attributeInfoLengthByte.toInt();
                if(attributeInfoLength == 0){
                    continue;
                }
                byte[] info = new byte[attributeInfoLength];
                codeBuf.get(info,0,attributeInfoLength);
                attributeInfo.setInfo(info);
            }
        }
    }
}
