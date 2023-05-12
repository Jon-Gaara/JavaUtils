package com.jon.bytecode.utils;

import com.jon.bytecode.type.U2;

import java.util.*;

/**
 * accessFlag Util
 * @author Maolin Yu
 */
public class ClassAccessFlagUtil {
    private static final Map<Integer,String> ACCESS_MAP = new HashMap<>();

    static {
        ACCESS_MAP.put(0x0001,"public");
        ACCESS_MAP.put(0x0010,"final");
        ACCESS_MAP.put(0x0020,"super");
        ACCESS_MAP.put(0x0200,"interface");
        ACCESS_MAP.put(0x0400,"abstract");
        ACCESS_MAP.put(0x1000,"synthetic");
        ACCESS_MAP.put(0x2000,"annotation");
        ACCESS_MAP.put(0x4000,"enum");
    }

    public static String classAccessFlagString(U2 flag){
        final int flagValue = flag.toInt();
        List<String> flagBuild = new ArrayList<>();
        ACCESS_MAP.keySet().forEach(key->{
            if((flagValue & key) == key){
                flagBuild.add(ACCESS_MAP.get(key));
            }
        });
        return String.join(",",flagBuild);
    }
}
