package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U4;

import java.nio.ByteBuffer;

/**
 * 根据《Java虚拟机规范》规定，CONSTANT_Integer_info常量存储一个整型数值，除一个tag字段外，只有一个U4类型的字段bytes，bytes转为10进制数就是这个常量所表示的整型值。
 *
 * @author Maolin Yu
 */
public class ConstantIntegerInfo extends CpInfo {

    private U4 bytes;

    public ConstantIntegerInfo(U1 tag) {
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        bytes = new U4(codeBuf.get(),codeBuf.get(), codeBuf.get(), codeBuf.get());
    }

    public U4 getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return "ConstantIntegerInfo{" +
                "bytes=" + bytes.toHexString() +
                '}';
    }
}
