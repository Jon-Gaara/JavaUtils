package com.jon.bytecode.asm.writer;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static aj.org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.GETSTATIC;

public class MainMethodWriter extends MethodVisitor {

    private MethodVisitor methodVisitor;

    protected MainMethodWriter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor);
        this.methodVisitor = methodVisitor;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        methodVisitor.visitFieldInsn(GETSTATIC,
                Type.getInternalName(System.class),
                "out",
                Type.getDescriptor(System.out.getClass()));
        methodVisitor.visitLdcInsn("hello word!");
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                Type.getInternalName(System.out.getClass()),
                "println",
                "(Ljava/lang/String;)V", false);
    }
}
