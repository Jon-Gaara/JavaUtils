package com.jon.image;

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
		try (FileInputStream fis = new FileInputStream(file2);
			ImageInputStream iis = ImageIO.createImageInputStream(fis)) {
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(fileType);
			// 只取第一个匹配的 reader：遍历所有 reader 会把多张图连续写进同一个输出流
			if (!readers.hasNext()) {
				throw new IllegalArgumentException("不支持的图片类型: " + fileType);
			}
			ImageReader reader = readers.next();
			try {
				reader.setInput(iis, true);
				BufferedImage buffer = reader.read(0);
				ImageIO.write(buffer, formatFileType, out);
			} finally {
				reader.dispose();
			}
		}
    }
    
    public static void main(String[] args) throws Exception {
    	String fileName="d:\\temporaryFile.pbm";
    	String formatFile = "d:\\temporaryFile.jpg";
    	String fileType="pnm";
    	String formatFileType="jpg";
    	try (FileOutputStream out = new FileOutputStream(new File(formatFile))) {
    		ImageFormatConversion.imageFormat(fileName, out, fileType, formatFileType);
    	}
	}
}