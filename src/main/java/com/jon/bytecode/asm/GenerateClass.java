package com.jon.bytecode.asm;

import cn.hutool.core.io.FileUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

public class GenerateClass {

    public static void main(String[] args) {
        generateClass(true);
    }

    public static void generateClass(boolean isGenerateMain){
        //ClassWriter.COMPUTE_MAXS   自动计算局部变量表和操作数栈的大小
        //ClassWriter.COMPUTE_FRAMES 自动计算方法的栈映射桢
        //ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        String className = "com.jon.bytecode.asm.AsmClass";
        String signature= "L"+ className.replace(".","/")+";";
        //自己计算方法的局部变量表和操作数栈的大小
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(Opcodes.V1_8, ACC_PUBLIC,className.replace(".","/"),signature,Object.class.getName().replace(".","/"),null);
        classWriter.visitEnd();

        //生成init方法
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC,"<init>","()V",null,null);
        //生成code属性
        methodVisitor.visitCode();
        //aload 父类无参构造方法
        methodVisitor.visitVarInsn(ALOAD,0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL,"java/lang/Object","<init>","()V",false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1,1);
        methodVisitor.visitEnd();

        //main方法
        if(isGenerateMain){
            MethodVisitor mainMethodVisitor = classWriter.visitMethod(ACC_PUBLIC|ACC_STATIC,"main","([Ljava/lang/String;)V",null,null);
            mainMethodVisitor.visitCode();
        }

        FieldVisitor fieldVisitor = classWriter.visitField(ACC_PRIVATE|ACC_STATIC|ACC_FINAL,"age","I",null,100);
        fieldVisitor.visitEnd();

        FieldVisitor fieldVisitor1 = classWriter.visitField(ACC_PUBLIC,"name","Ljava/lang/String;",null,null);
        fieldVisitor1.visitAnnotation("Llombok/Getter;",false);
        fieldVisitor.visitEnd();
        byte[] byteCode = classWriter.toByteArray();
        FileUtil.writeBytes(byteCode,className.replace(".", "/")+".class");
    }
}
