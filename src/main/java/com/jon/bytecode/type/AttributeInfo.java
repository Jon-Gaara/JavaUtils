package com.jon.bytecode.type;

/**
 * Java class file Attribute Info
 * @author Maolin Yu
 */
public class AttributeInfo {
    private U2 attributeNameIndex;
    private U4 attributeLength;
    private byte[] info;

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

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }
}
