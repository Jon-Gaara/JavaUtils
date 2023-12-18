package com.jon.bytecode.type.constant;

import cn.hutool.core.util.ByteUtil;
import com.jon.bytecode.type.U1;

/**
 * CONSTANT_Float_info与CONSTANT_Integer_info在存储结构上是一样的，只是bytes所表示的内容不同，CONSTANT_Float_info的bytes存储的是浮点数
 * @author Maolin Yu
 */
public class ConstantFloatInfo extends ConstantIntegerInfo {

    public ConstantFloatInfo(U1 tag) {
        super(tag);
    }

    @Override
    public String toString() {
        return "ConstantFloatInfo{" +
                "bytes=" + super.getBytes().toHexString() +
                '}';
    }
}
