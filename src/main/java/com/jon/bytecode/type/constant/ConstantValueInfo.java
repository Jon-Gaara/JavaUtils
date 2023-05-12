package com.jon.bytecode.type.constant;

import com.jon.bytecode.type.U2;
import com.jon.bytecode.type.U4;

/**
 * @author Maolin Yu
 */
public class ConstantValueInfo {
    private U2 attributeNameIndex;
    private U4 attributeLength;
    private U2 constantValueIndex;

    public U2 getAttributeNameIndex() {
        return attributeNameIndex;
    }

    public void setAttributeNameIndex(U2 attributeNameIndex) {
        this.attributeNameIndex = attributeNameIndex;
    }

    public U4 getAttributeLength() {
        return attributeLength;
    }

    public void setAttributeLength(U4 attributeLength) {
        this.attributeLength = attributeLength;
    }

    public U2 getConstantValueIndex() {
        return constantValueIndex;
    }

    public void setConstantValueIndex(U2 constantValueIndex) {
        this.constantValueIndex = constantValueIndex;
    }
}
