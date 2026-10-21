package com.jon.bytecode.asm;

import cn.hutool.core.io.FileUtil;
import com.jon.bytecode.asm.writer.MainClassWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class OperationClass {

    public static void main(String[] args) throws IOException {
        String className = "com.jon.bytecode.asm.AsmClass";
        String filePath = className.replace(".", "/")+".class";
        if(!FileUtil.exist(filePath)){
            GenerateClass.generateClass(false);
        }
        // 直接读取刚生成的 .class 文件字节，避免 ClassReader(String) 经类加载器找不到文件
        ClassReader classReader = new ClassReader(FileUtil.readBytes(filePath));
        MainClassWriter classWriter = new MainClassWriter(new ClassWriter(0));
        classReader.accept(classWriter,0);
        FieldVisitor fieldVisitor = classWriter.visitField(ACC_PRIVATE,
                "nickName", "Ljava/lang/String;", null, null);
        fieldVisitor.visitAnnotation("Llombok/Getter;", false);
        fieldVisitor.visitEnd();
        //main方法
        MethodVisitor mainMethodVisitor = classWriter.visitMethod(ACC_PUBLIC|ACC_STATIC,"main","([Ljava/lang/String;)V",null,null);
        mainMethodVisitor.visitCode();
        mainMethodVisitor.visitInsn(RETURN);
        mainMethodVisitor.visitMaxs(0,1);
        mainMethodVisitor.visitEnd();
        byte[] newByteCode = classWriter.toByteArray();
        FileUtil.writeBytes(newByteCode,filePath);
    }
}
