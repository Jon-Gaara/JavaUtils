package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.U1;

/**
 * @author Maolin Yu
 */
public class ConstantDoubleInfo extends ConstantLongInfo {
    public ConstantDoubleInfo(U1 tag) {
        super(tag);
    }

    @Override
    public String toString() {
        return "ConstantDoubleInfo{" +
                "highBytes=" + super.getHighBytes().toHexString() +
                ", lowBytes=" + super.getLowBytes().toHexString() +
                '}';
    }
}
