package com.jon.bytecode.utils;

import com.jon.bytecode.handle.*;
import com.jon.bytecode.type.ClassFile;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Java class file analysis
 * @author Maolin Yu
 */
public class ClassFileAnalysisUtil {

    private final static List<BaseByteCodeHandle> HANDLES = new ArrayList<>();

    static{
        HANDLES.add(new MagicHandle());
        HANDLES.add(new VersionHandle());
        HANDLES.add(new ConstantPoolHandle());
        HANDLES.add(new AccessFlagHandle());
        HANDLES.add(new ThisAndSuperClassHandler());
        HANDLES.add(new InterfacesHandle());
        HANDLES.add(new FieldHandler());
        HANDLES.add(new MethodHandle());
        HANDLES.add(new AttributesHandle());
        HANDLES.sort(Comparator.comparing(BaseByteCodeHandle::order));
    }

    public static ClassFile analysis(ByteBuffer codeBuf) throws Exception{
        //重置读取指针
        codeBuf.position(0);
        ClassFile classFile = new ClassFile();
        for(BaseByteCodeHandle handle : HANDLES){
            handle.read(codeBuf,classFile);
        }
        return classFile;
    }
}
