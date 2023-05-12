package com.jon.bytecode.type;

import com.jon.bytecode.handle.ConstantInfoHandle;
import com.jon.bytecode.type.constant.*;

/**
 * constant pool
 * @author Maolin Yu
 */
public abstract class CpInfo implements ConstantInfoHandle {

    private U1 tag;

    public CpInfo(U1 tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "tag=" + tag.toString();
    }

    public static CpInfo newCpInfo(U1 tag){
        int tagValue = tag.toInt();
        CpInfo cpInfo;
        switch (tagValue){
            case 1:
                cpInfo = new ConstantUtf8Info(tag);
                break;
            case 3:
                cpInfo = new ConstantIntegerInfo(tag);
                break;
            case 4:
                cpInfo = new ConstantFloatInfo(tag);
                break;
            case 5:
                cpInfo = new ConstantLongInfo(tag);
                break;
            case 6:
                cpInfo = new ConstantDoubleInfo(tag);
                break;
            case 7:
                cpInfo = new ConstantClassInfo(tag);
                break;
            case 8:
                cpInfo = new ConstantStringInfo(tag);
                break;
            case 9:
                cpInfo = new ConstantFieldrefInfo(tag);
                break;
            case 10:
                cpInfo = new ConstantMethodrefInfo(tag);
                break;
            case 11:
                cpInfo = new ConstantInterfaceMethodrefInfo(tag);
                break;
            case 12:
                cpInfo = new ConstantNameAndTypeInfo(tag);
                break;
            case 15:
                cpInfo = new ConstantMethodHandleInfo(tag);
                break;
            case 16:
                cpInfo = new ConstantMethodTypeInfo(tag);
                break;
            case 18:
                cpInfo = new ConstantInvokeDynamicInfo(tag);
                break;
            default:
                throw new RuntimeException("not found constant type tag="+tagValue);
        }
        return cpInfo;
    }
}
