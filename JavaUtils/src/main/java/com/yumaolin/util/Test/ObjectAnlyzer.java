package com.yumaolin.util.Test;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class ObjectAnlyzer {
    private ArrayList<Object> list = new ArrayList<Object>();
    
    public String toString(Object obj){
	
	if(obj==null)return "null";
	if(list.contains(obj)) return "...";
	list.add(obj);
	Class c1 = obj.getClass();
	if(c1==String.class) return (String) obj;
	if(c1.isArray()){
	    //可以返回表示数组类型的Class
	    String r=c1.getComponentType()+"[]{";
	    for(int i=0;i<Array.getLength(obj);i++){
		if(i>0) r+=",";
		Object val = Array.get(obj, i);
		//判断指定的Class类是否为一个基本类型
		if(c1.getComponentType().isPrimitive()) r+=val;
		else r+=toString(val);
	    }
	    return r+"}";
	}
	
	String r = c1.getName();//获取Class的名称
	do{   
	     r+="[";
	     Field[] fields = c1.getDeclaredFields();//获取当前类的所有域
	     
	     /*
	      * 反射对象设置可访问属性.flag为true表明屏蔽java语言的访问检测,使得对象的私有属性也可以查询和访问
	      */
	     AccessibleObject.setAccessible(fields, true);
	     
	     //获取所有的域的名字和值
	     for(Field field:fields){
		 //判断域是不是静态域
		 if(!Modifier.isStatic(field.getModifiers())){
		     if(!r.endsWith("[")){
			 r+=",";
		     }
		     r+=field.getName()+"=";
		     try{
			Class t = field.getType();
			Object val = field.get(obj); 
			if(t.isPrimitive()) r+=val;
			else r+=toString(val);
		     }catch(Exception e){
			 e.printStackTrace();
		     }
		 }
	     }
	     r+="]";
	     c1=c1.getSuperclass();
	}while(c1!=null);
	return r;
    }
    
    /**
     * 对任意类型的数组进行扩容
     */
    public Object copyOf(Object obj,int newLength){
	Class c1 = obj.getClass();
	if(!c1.isArray()) return null;
	Class componentType = c1.getComponentType();
	System.out.println("type : "+componentType);
	int length = Array.getLength(obj);
	Object newArray = Array.newInstance(componentType,newLength);
	System.arraycopy(obj,0,newArray,0,Math.min(length, newLength));
	return newArray;
    }
}
