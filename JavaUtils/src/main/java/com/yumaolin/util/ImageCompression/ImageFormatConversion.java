package com.yumaolin.util.ImageCompression;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * http://www.360doc.com/content/14/0418/09/14416931_369953357.shtml
 * 接口文档:http://jai-imageio.github.io/jai-imageio-core/javadoc/
 * jai 图片格式转换  jai-imageio-core-1.3.1.jar
 * 支持的图片格式:bmp,clib,gif,jpeg,pcx,png,pnm(PBM,PGM,PPM),raw,tiff,wbmp,jpeg2000,jpeg-ls
 * 
 * 其他 开源api TwelveMonkeys：https://github.com/haraldk/TwelveMonkeys
 */
public class ImageFormatConversion {
    
    public static void imageFormat(String fileName,OutputStream out,String fileType,String formatFileType) throws Exception{
    	File file2 = new File(fileName);
	FileInputStream fis = new FileInputStream(file2);
	Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(fileType);
	ImageInputStream iis = ImageIO.createImageInputStream(fis);
	while (readers.hasNext()){
		ImageReader reader = (ImageReader)readers.next();
		reader.setInput(iis,true);
		BufferedImage buffer = reader.read(0);
		ImageIO.write(buffer,formatFileType,out); 
	}
    }
    
    public static void main(String[] args) throws Exception {
    	String fileName="d:\\temporaryFile.pbm";
    	String formatFile = "d:\\temporaryFile.jpg";
    	FileOutputStream out = new FileOutputStream(new File(formatFile));
    	String fileType="pnm";
    	String formatFileType="jpg";
    	ImageFormatConversion.imageFormat(fileName,out,fileType,formatFileType);
	}
}