package com.jon.bytecode.utils;

import com.jon.bytecode.type.U2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maolin Yu
 */
public class MethodAccessFlagUtil {
    private static final Map<Integer,String> ACCESS_MAP = new HashMap<>();

    static {
        ACCESS_MAP.put(0x0001,"public");
        ACCESS_MAP.put(0x0002,"private");
        ACCESS_MAP.put(0x0004,"protected");
        ACCESS_MAP.put(0x0008,"static");
        ACCESS_MAP.put(0x0010,"final");
        ACCESS_MAP.put(0x0020,"synchronized");
        ACCESS_MAP.put(0x0040,"bridge");
        ACCESS_MAP.put(0x0080,"varargs");
        ACCESS_MAP.put(0x0100,"native");
        ACCESS_MAP.put(0x0400,"abstract");
        ACCESS_MAP.put(0x0800,"strict");
        ACCESS_MAP.put(0x1000,"synthetic");
    }

    public static String methodAccessFlagString(U2 flag){
        final  int flagValue = flag.toInt();
        List<String> flagBuild = new ArrayList<>();
        ACCESS_MAP.keySet().forEach(key->{
            if((flagValue & key) == key){
                flagBuild.add(ACCESS_MAP.get(key));
            }
        });
        return String.join(",",flagBuild);
    }
}
