package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.CpInfo;
import com.jon.bytecode.type.U1;
import com.jon.bytecode.type.U2;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
/**
 * 根据《Java虚拟机规范》规定，CONSTANT_Utf8_info常量结构用于存储字符串常量，字符串编码使用UTF-8。
 * 除一个必须的tag字段和存储字符串的字节数组外，还要有一个字段存储描述这个字符串字节数组的长度
 */
public class ConstantUtf8Info extends CpInfo {
    private U2 length;
    private byte[] bytes;

    public ConstantUtf8Info(U1 tag) {
        super(tag);
    }

    @Override
    public void read(ByteBuffer codeBuf) throws Exception {
        length = new U2(codeBuf.get(),codeBuf.get());
        bytes = new byte[length.toInt()];
        codeBuf.get(bytes,0, length.toInt());
    }

    public U2 getLength() {
        return length;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return "ConstantUtf8Info{" +
                "length=" + length +
                ", bytes=" + new String(bytes, StandardCharsets.UTF_8) +
                '}';
    }
}
