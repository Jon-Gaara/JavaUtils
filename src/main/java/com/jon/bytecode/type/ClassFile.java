package com.jon.bytecode.type;

/**
 * class file info
 * @author Maolin Yu
 */
public class ClassFile {
    /**
     * 魔数
     */
    private U4 magic;
    /**
     *  副版本号
     */
    private U2 minorVersion;
    /**
     *  主版本号
     */
    private U2 majorVersion;
    /**
     * 常量池计数器
     */
    private U2 constantPoolCount;
    /**
     * 常量池
     */
    private CpInfo[] constantPool;
    /**
     * 访问标志
     */
    private U2 accessFlags;
    /**
     * 类索引
     */
    private U2 thisClass;
    /**
     * 父类索引
     */
    private U2 superClass;
    /**
     * 接口总数
     */
    private U2 interfacesCount;
    /**
     * 接口数组
     */
    private U2[] interfaces;
    /**
     * 字段总数
     */
    private U2 fieldsCount;
    /**
     * 字段表
     */
    private FieldInfo[] fields;
    /**
     * 方法总数
     */
    private U2 methodsCount;
    /**
     * 方法表
     */
    private MethodInfo[] methods;
    /**
     * 属性总数
     */
    private U2 attributesCount;
    /**
     * 属性表
     */
    private AttributeInfo[] attributes;

    public U4 getMagic() {
        return magic;
    }

    public void setMagic(U4 magic) {
        this.magic = magic;
    }

    public U2 getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(U2 minorVersion) {
        this.minorVersion = minorVersion;
    }

    public U2 getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(U2 majorVersion) {
        this.majorVersion = majorVersion;
    }

    public U2 getConstantPoolCount() {
        return constantPoolCount;
    }

    public void setConstantPoolCount(U2 constantPoolCount) {
        this.constantPoolCount = constantPoolCount;
    }

    public CpInfo[] getConstantPool() {
        return constantPool;
    }

    public void setConstantPool(CpInfo[] constantPool) {
        this.constantPool = constantPool;
    }

    public U2 getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(U2 accessFlags) {
        this.accessFlags = accessFlags;
    }

    public U2 getThisClass() {
        return thisClass;
    }

    public void setThisClass(U2 thisClass) {
        this.thisClass = thisClass;
    }

    public U2 getSuperClass() {
        return superClass;
    }

    public void setSuperClass(U2 superClass) {
        this.superClass = superClass;
    }

    public U2 getInterfacesCount() {
        return interfacesCount;
    }

    public void setInterfacesCount(U2 interfacesCount) {
        this.interfacesCount = interfacesCount;
    }

    public U2[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(U2[] interfaces) {
        this.interfaces = interfaces;
    }

    public U2 getFieldsCount() {
        return fieldsCount;
    }

    public void setFieldsCount(U2 fieldsCount) {
        this.fieldsCount = fieldsCount;
    }

    public FieldInfo[] getFields() {
        return fields;
    }

    public void setFields(FieldInfo[] fields) {
        this.fields = fields;
    }

    public U2 getMethodsCount() {
        return methodsCount;
    }

    public void setMethodsCount(U2 methodsCount) {
        this.methodsCount = methodsCount;
    }

    public MethodInfo[] getMethods() {
        return methods;
    }

    public void setMethods(MethodInfo[] methods) {
        this.methods = methods;
    }

    public U2 getAttributesCount() {
        return attributesCount;
    }

    public void setAttributesCount(U2 attributesCount) {
        this.attributesCount = attributesCount;
    }

    public AttributeInfo[] getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributeInfo[] attributes) {
        this.attributes = attributes;
    }
}
