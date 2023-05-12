package com.jon.bytecode;

import com.jon.bytecode.type.*;
import com.jon.bytecode.type.constant.ConstantClassInfo;
import com.jon.bytecode.type.constant.ConstantIntegerInfo;
import com.jon.bytecode.type.constant.ConstantUtf8Info;
import com.jon.bytecode.type.constant.ConstantValueInfo;
import com.jon.bytecode.utils.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ClassFileAnalysisTest {

    public  ByteBuffer readFile(String classFilePath) throws Exception {
        File file = new File(classFilePath);
        if (!file.exists()) {
            throw new Exception("file not exists!");
        }
        byte[] byteCodeBuf = new byte[(int) file.length()];
        int length;
        try (InputStream in = new FileInputStream(file)) {
            length = in.read(byteCodeBuf);
        }
        if (length < 1) {
            throw new Exception("not read byte code.");
        }
        // 将字节数组包装为ByteBuffer
        return ByteBuffer.wrap(byteCodeBuf, 0, length).asReadOnlyBuffer();
    }

    @Test
    public  void testAnalysis() throws Exception {
        // 读取class文件
        String classFilePath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("com/jon/thread/Bank.class")).getPath();
        ByteBuffer codeBuf = readFile(classFilePath);
        // 解析class文件
        ClassFile classFile = ClassFileAnalysisUtil.analysis(codeBuf);
        // 打印魔数解析器解析出来的Magic
        System.out.println("magic: "+classFile.getMagic().toHexString());
        //打印副版本号
        System.out.println("minorVersion: "+classFile.getMinorVersion().toInt());
        //打印主版本号
        System.out.println("majorVersion: "+classFile.getMajorVersion().toInt());
        //解析常量池
        int cpInfoCount = classFile.getConstantPoolCount().toInt();
        System.out.println("ConstantPoolCount: "+cpInfoCount);
        CpInfo[] cpInfos = classFile.getConstantPool();
        for(int i=0;i<cpInfos.length;i++){
            if(cpInfos[i] == null){
                continue;
            }
            System.out.println("#"+i+":"+cpInfos[i]);
        }
        //解析accessFlag
        U2 accessFlag = classFile.getAccessFlags();
        System.out.println("accessFlag: " + ClassAccessFlagUtil.classAccessFlagString(accessFlag));
        //解析this
        U2 thisClass = classFile.getThisClass();
        ConstantUtf8Info thisClassName = getName(thisClass.toInt(),cpInfos);
        System.out.println("thisClass: " + thisClassName);
        //解析super
        U2 superClass = classFile.getSuperClass();
        ConstantUtf8Info superClassName = getName(superClass.toInt(),cpInfos);
        System.out.println("superClass: " + superClassName);
        //解析interface
        int interfaceCount = classFile.getInterfacesCount().toInt();
        System.out.println("interfaceCount: " + interfaceCount);
        for(U2 u2 : classFile.getInterfaces()){
            ConstantUtf8Info interfaceClassName = getName(u2.toInt(),cpInfos);
            System.out.println("interfaceClass: " + interfaceClassName);
        }
        //解析 field
        int fieldCount = classFile.getFieldsCount().toInt();
        System.out.println("fieldCount:"+fieldCount);
        FieldInfo[] fieldInfos = classFile.getFields();
        for(FieldInfo fieldInfo : fieldInfos){
            System.out.println("fieldAccessFlag:" + FieldAccessFlagUtil.fieldAccessFlagString(fieldInfo.getAccessFlag()));
            System.out.println("fieldName: " + cpInfos[fieldInfo.getNameIndex().toInt() - 1]);
            System.out.println("fieldDescriptorName: " + cpInfos[fieldInfo.getDescriptorIndex().toInt() - 1]);
            System.out.println("fieldAttributeCount: " + fieldInfo.getAttributesCount().toInt());
            fieldAttribute(fieldInfo,classFile);
        }
        //解析 method
        int methodCount = classFile.getMethodsCount().toInt();
        System.out.println("methodCount: " + methodCount);
        MethodInfo[] methodInfos = classFile.getMethods();
        for(MethodInfo methodInfo : methodInfos){
            System.out.println("methodAccessFlag: " + MethodAccessFlagUtil.methodAccessFlagString(methodInfo.getAccessFlags()));
            System.out.println("methodName: " + cpInfos[methodInfo.getNameIndex().toInt() - 1]);
            System.out.println("methodDescriptor: " + cpInfos[methodInfo.getDescriptorIndex().toInt() - 1]);
            System.out.println("methodAttributeCount: " + methodInfo.getAttributesCount().toInt());
            methodAttribute(methodInfo,classFile);
        }

    }

    private ConstantUtf8Info getName(int index,CpInfo[] cpInfos){
        ConstantClassInfo constantClassInfo = (ConstantClassInfo) cpInfos[index - 1];
        return (ConstantUtf8Info) cpInfos[constantClassInfo.getNameIndex().toInt() - 1];
    }

    private void fieldAttribute(FieldInfo fieldInfo,ClassFile classFile){
        AttributeInfo[] attributeInfos = fieldInfo.getAttributes();
        if(attributeInfos == null || attributeInfos.length == 0){
            return;
        }
        for(AttributeInfo attributeInfo : attributeInfos){
            ConstantUtf8Info nameInfo = (ConstantUtf8Info) classFile.getConstantPool()[attributeInfo.getAttributeNameIndex().toInt() - 1];
            String name = new String(nameInfo.getBytes());
            if(name.equalsIgnoreCase("ConstantValue")){
                ConstantValueInfo constantValueInfo = AttributeProcessingUtil.constantValue(attributeInfo);
                CpInfo cvInfo = classFile.getConstantPool()[constantValueInfo.getConstantValueIndex().toInt() - 1];
                if(cvInfo instanceof ConstantUtf8Info){
                    System.out.println("constantValue:"+ cvInfo);
                }else if(cvInfo instanceof ConstantIntegerInfo){
                    System.out.println("constantValue:"+((ConstantIntegerInfo) cvInfo).getBytes().toInt());
                }
            }
        }
    }

    private void methodAttribute(MethodInfo methodInfo,ClassFile classFile){
        AttributeInfo[] attributeInfos = methodInfo.getAttributeInfos();
        if(attributeInfos == null || attributeInfos.length == 0){
            return;
        }
        for (AttributeInfo attributeInfo : attributeInfos){
            ConstantUtf8Info nameInfo = (ConstantUtf8Info) classFile.getConstantPool()[attributeInfo.getAttributeNameIndex().toInt() - 1];
            String name = new String(nameInfo.getBytes());
            if(name.equalsIgnoreCase("code")){
                CodeAttribute codeAttribute = AttributeProcessingUtil.code(attributeInfo);
                System.out.println("maxStack:"+codeAttribute.getMaxStack().toInt());
                System.out.println("maxLocals:"+codeAttribute.getMaxLocals().toInt());
                System.out.println("codeLength:"+codeAttribute.getCodeLength().toInt());
                System.out.print("code:");
                for (byte b : codeAttribute.getCode()){
                    System.out.print((b & 0xff) + "");
                }
                System.out.println();
            }
        }
    }
}
