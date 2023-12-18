package com.jon.bytecode.asm.proxy;

import jdk.internal.org.objectweb.asm.Type;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

/**
 * 参考: https://github.com/wujiuye/bytecode-book/blob/master/asm-bytecode-project/src/main/java/com/wujiuye/asmbytecode/book/sixth/jdk/MyProxy.java
 * @author Maolin Yu
 */
public class MyProxyFactory {

    public static byte[] createProxyClass(String className, Class<?>[] interfaces) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classWriter.visit(Opcodes.V1_8, ACC_PUBLIC, className, null, Type.getInternalName(MyProxy.class),
            getInternalNames(interfaces));
        // 添加带参数的构造方法
        createInitMethod(classWriter);
        // 实现接口方法
        for (Class<?> interfaceClass : interfaces) {
            interfaceMethod(classWriter, className, interfaceClass);
        }
        // 添加静态代码快，获取method
        addStaticBlock(classWriter, className, interfaces);
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    private static void addReturnType(MethodVisitor methodVisitor, Class returnType) {
        if (returnType == void.class) {
            // 返回return指令
            methodVisitor.visitInsn(RETURN);
        } else if (returnType == int.class) {
            // 先将invoke方法返回值由Object转成Integer类型
            methodVisitor.visitTypeInsn(CHECKCAST, Type.getInternalName(Integer.class));
            // 调用Integer的intValue方法
            methodVisitor.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Integer.class), "intValue", "()I", false);
            methodVisitor.visitInsn(IRETURN);
        } else {
            methodVisitor.visitTypeInsn(CHECKCAST, Type.getInternalName(returnType));
            methodVisitor.visitInsn(ARETURN);
        }
    }

    private static void addStaticBlock(ClassWriter classWriter, String className, Class<?>[] interfaces) {
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
        methodVisitor.visitCode();
        for (Class clazz : interfaces) {
            Method[] methods = clazz.getMethods();
            for (int i = 0, k = methods.length; i < k; i++) {
                Method method = methods[i];
                // 调用Class的forName获取一个Class实例
                String fieldName = "m" + i;
                methodVisitor.visitLdcInsn(clazz.getName());
                methodVisitor.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Class.class), "forName",
                    "(Ljava/lang/String;)Ljava/lang/Class;", false);
                // 调用Class的getMethod方法
                methodVisitor.visitLdcInsn(method.getName());

                Class[] methodParameterTypes = method.getParameterTypes();
                if (methodParameterTypes.length == 0) {
                    methodVisitor.visitInsn(ACONST_NULL);
                } else {
                    switch (methodParameterTypes.length) {
                        case 1:
                            methodVisitor.visitInsn(ICONST_1);
                            break;
                        case 2:
                            methodVisitor.visitInsn(ICONST_2);
                            break;
                        case 3:
                            methodVisitor.visitInsn(ICONST_3);
                            break;
                        default:
                            methodVisitor.visitVarInsn(BIPUSH, methodParameterTypes.length);
                    }
                    methodVisitor.visitTypeInsn(ANEWARRAY, Type.getInternalName(Class.class));
                    // 为数组元素赋值
                    for (int j = 0, l = methodParameterTypes.length; j < l; j++) {
                        methodVisitor.visitInsn(DUP);
                        switch (j) {
                            case 0:
                                methodVisitor.visitInsn(ICONST_0);
                                break;
                            case 1:
                                methodVisitor.visitInsn(ICONST_1);
                                break;
                            case 2:
                                methodVisitor.visitInsn(ICONST_2);
                                break;
                            case 3:
                                methodVisitor.visitInsn(ICONST_3);
                                break;
                            default:
                                methodVisitor.visitVarInsn(BIPUSH, i);
                                break;
                        }
                        methodVisitor.visitLdcInsn(methodParameterTypes[j].getName());
                        // 调用forName获取参数的Class实例
                        methodVisitor.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Class.class), "forName",
                            "(Ljava/lang/String;)Ljava/lang/Class;", false);
                        // 存储到数组
                        methodVisitor.visitInsn(AASTORE);
                    }
                }
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Class.class), "getMethod",
                    "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
                // 为静态字段赋值
                methodVisitor.visitFieldInsn(PUTSTATIC, className, fieldName, Type.getDescriptor(Method.class));
            }
        }
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();
    }

    private static void interfaceMethod(ClassWriter classWriter, String className, Class<?> interfaceClass) {
        Method[] methods = interfaceClass.getMethods();
        for (int i = 0, k = methods.length; i < k; i++) {
            Method method = methods[i];
            // 添加静态字段
            classWriter.visitField(ACC_PRIVATE | ACC_STATIC, "m" + i, Type.getDescriptor(Method.class), null, null);
            MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, method.getName(),
                Type.getMethodDescriptor(method), null, new String[] {Type.getInternalName(Exception.class)});
            methodVisitor.visitCode();

            Label form = new Label();
            Label to = new Label();
            Label target = new Label();
            methodVisitor.visitLabel(form);

            // 获取父类的字段，字段名为h，类型为InvocationHandler
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, Type.getInternalName(MyProxy.class), "h",
                Type.getDescriptor(InvocationHandler.class));

            // 调用InvocationHandler invoke方法的三个参数
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETSTATIC, className, "m" + i, Type.getDescriptor(Method.class));
            int paramCount = method.getParameterCount();
            if (paramCount == 0) {
                methodVisitor.visitInsn(ACONST_NULL);
            } else {
                switch (paramCount) {
                    case 1:
                        methodVisitor.visitInsn(ICONST_1);
                        break;
                    case 2:
                        methodVisitor.visitInsn(ICONST_2);
                        break;
                    case 3:
                        methodVisitor.visitInsn(ICONST_3);
                        break;
                    default:
                        methodVisitor.visitVarInsn(BIPUSH, paramCount);
                }
                methodVisitor.visitTypeInsn(ANEWARRAY, Type.getInternalName(Object.class));
                for (int j = 1; j <= paramCount; j++) {
                    methodVisitor.visitInsn(DUP);
                    switch (j - 1) {
                        case 0:
                            methodVisitor.visitInsn(ICONST_0);
                            break;
                        case 1:
                            methodVisitor.visitInsn(ICONST_1);
                            break;
                        case 2:
                            methodVisitor.visitInsn(ICONST_2);
                            break;
                        case 3:
                            methodVisitor.visitInsn(ICONST_3);
                            break;
                        default:
                            methodVisitor.visitVarInsn(BIPUSH, i - 1);
                    }
                    // 暂不考虑参数类型为基本类型的情况
                    methodVisitor.visitVarInsn(ALOAD, j);
                    methodVisitor.visitInsn(AASTORE);
                }
            }
            // 调用invoke方法
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(InvocationHandler.class), "invoke",
                "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", true);
            // 添加return指令
            addReturnType(methodVisitor, method.getReturnType());

            methodVisitor.visitLabel(to);
            methodVisitor.visitLabel(target);
            // 抛出异常
            methodVisitor.visitInsn(ATHROW);
            methodVisitor.visitTryCatchBlock(form, to, target, Type.getInternalName(Exception.class));
            methodVisitor.visitFrame(F_FULL, 0, null, 0, null);
            methodVisitor.visitMaxs(1,1);
            methodVisitor.visitEnd();
        }
    }

    private static void createInitMethod(ClassWriter classWriter) {
        MethodVisitor methodVisitor =
            classWriter.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/reflect/InvocationHandler;)V", null, null);
        methodVisitor.visitCode();
        // 获取this字段
        methodVisitor.visitVarInsn(ALOAD, 0);
        // 获取InvocationHandler引用
        methodVisitor.visitVarInsn(ALOAD, 1);
        // 调用父类构造方法
        methodVisitor.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(MyProxy.class), "<init>",
            "(Ljava/lang/reflect/InvocationHandler;)V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(2,2);
        methodVisitor.visitEnd();
    }

    private static String[] getInternalNames(Class<?>[] interfaces) {
        String[] internalNames = new String[interfaces.length];
        for (int i = 0, k = interfaces.length; i < k; i++) {
            internalNames[i] = Type.getInternalName(interfaces[i]);
        }
        return internalNames;
    }

}
