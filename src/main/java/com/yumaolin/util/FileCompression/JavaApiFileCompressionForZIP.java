package com.yumaolin.util.FileCompression;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
 *Java api 进行ZIP文件压缩,文件（夹）名称带中文时，出现乱码问题
 */
public class JavaApiFileCompressionForZIP {
 public File FileCompressionForZIP(String dataSource,String zipFilePath) throws IOException{
	 File file= new File(dataSource);
	 File zipFile = new File(zipFilePath);
	 ZipOutputStream zipOut=null;
	 try{
		OutputStream out = new FileOutputStream(zipFile);
		BufferedOutputStream buf = new BufferedOutputStream(out);
		zipOut = new ZipOutputStream(buf,StandardCharsets.UTF_8);//jdk1.7支持
		String basePath=null;
		//获取目录
		 if(file.isDirectory()){
			 basePath = file.getPath();
		 }else{
			 basePath = file.getParent();
		 }
		 zipFile(file,basePath,zipOut);
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(zipOut!=null){
			 zipOut.closeEntry();
			 zipOut.close();
		 }
	 }
	 return zipFile;
 }
 
 private void zipFile(File file,String basePath,ZipOutputStream zipOut) throws IOException{
	 File[] files = null;
	 if(file.isDirectory()){
		 files = file.listFiles();
	 }else{
		 files = new File[1];
		 files[0] = file;
	 }
	 InputStream is = null;
	 String pathName;
	 byte[] buff = new byte[1024];
	 int length=0;
	 try{
	     	 if(files==null){
	     	    return; 
	     	 }
		 for(File newFile:files){
			 if(newFile.isDirectory()){
				 pathName = newFile.getPath().substring(basePath.length()+1)+File.separator;//不带目录压缩
				 zipOut.putNextEntry(new ZipEntry(pathName));
				 zipFile(file,basePath,zipOut);
			 }else{
				 //pathName =  newFile.getPath().substring(basePath.length()+1);
				 pathName = newFile.getPath();
				 is = new FileInputStream(pathName);
				 BufferedInputStream buffer = new BufferedInputStream(is);
				 //zipOut.putNextEntry(new ZipEntry(pathName));带目录压缩
				 zipOut.putNextEntry(new ZipEntry(newFile.getName()));//不带目录压缩
				 //zipOut.setLevel(9);设置zip压缩级别 0~9
				 while((length = buffer.read(buff))!=-1){
					 zipOut.write(buff,0,length);
				 }
				 buffer.close();
			 }
		 }
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(is!=null){
			 is.close();
		 }
	 }
 }
}
