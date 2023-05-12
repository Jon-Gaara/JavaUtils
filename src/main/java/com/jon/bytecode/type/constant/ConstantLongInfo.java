package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U4;

import java.nio.ByteBuffer;

/**
 * 与CONSTANT_Integer_info常量不同的是，CONSTANT_Long_info常量使用8个字节存储一个长整型数值，即使用两个U4类型的字段分别存储一个长整型数的高32位和低32位
 *
 * @author Maolin Yu
 */
public class ConstantLongInfo extends CpInfo {

    private U4 highBytes;

    private U4 lowBytes;

    public ConstantLongInfo(U1 tag) {
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        //读取高32位
        highBytes = new U4(codeBuf.get(),codeBuf.get(),codeBuf.get(),codeBuf.get());
        //读取低32位
        lowBytes = new U4(codeBuf.get(),codeBuf.get(),codeBuf.get(),codeBuf.get());
    }

    public U4 getHighBytes() {
        return highBytes;
    }

    public U4 getLowBytes() {
        return lowBytes;
    }

    @Override
    public String toString() {
        return "ConstantLongInfo{" +
                "highBytes=" + highBytes.toHexString() +
                ", lowBytes=" + lowBytes.toHexString() +
                '}';
    }
}
