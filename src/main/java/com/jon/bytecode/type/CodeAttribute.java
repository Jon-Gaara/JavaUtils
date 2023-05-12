package com.jon.bytecode.type;

public class CodeAttribute {
    private U2 attributeNameIndex;
    private U4 attributeLength;
    private U2 maxStack;
    private U2 maxLocals;
    private U4 codeLength;
    private byte[] code;
    private U4 exceptionTableLength;
    private Exception[] exceptionTable;
    private U2 attributeCount;
    private AttributeInfo[] attributeInfos;

    public static class Exception{
        private U2 startPc;
        private U2 endPc;
        private U2 handlePc;
        private U2 catchType;

        public U2 getStartPc() {
            return startPc;
        }

        public void setStartPc(U2 startPc) {
            this.startPc = startPc;
        }

        public U2 getEndPc() {
            return endPc;
        }

        public void setEndPc(U2 endPc) {
            this.endPc = endPc;
        }

        public U2 getHandlePc() {
            return handlePc;
        }

        public void setHandlePc(U2 handlePc) {
            this.handlePc = handlePc;
        }

        public U2 getCatchType() {
            return catchType;
        }

        public void setCatchType(U2 catchType) {
            this.catchType = catchType;
        }
    }

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

    public U2 getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(U2 maxStack) {
        this.maxStack = maxStack;
    }

    public U2 getMaxLocals() {
        return maxLocals;
    }

    public void setMaxLocals(U2 maxLocals) {
        this.maxLocals = maxLocals;
    }

    public U4 getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(U4 codeLength) {
        this.codeLength = codeLength;
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    public U4 getExceptionTableLength() {
        return exceptionTableLength;
    }

    public void setExceptionTableLength(U4 exceptionTableLength) {
        this.exceptionTableLength = exceptionTableLength;
    }

    public Exception[] getExceptionTable() {
        return exceptionTable;
    }

    public void setExceptionTable(Exception[] exceptionTable) {
        this.exceptionTable = exceptionTable;
    }

    public U2 getAttributeCount() {
        return attributeCount;
    }

    public void setAttributeCount(U2 attributeCount) {
        this.attributeCount = attributeCount;
    }

    public AttributeInfo[] getAttributeInfos() {
        return attributeInfos;
    }

    public void setAttributeInfos(AttributeInfo[] attributeInfos) {
        this.attributeInfos = attributeInfos;
    }
}
