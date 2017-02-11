package com.yumaolin.util.ImageCompression;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * Thumbnailator 默认格式为JPEG格式
 * 高清图的内存溢出OOM问题只能使用ImageMagick转换了。
 * 对于CMYK模式的图像，由于JDK的Bug，目前还不能够处理
 * @author yumaolin
 */
public class ThumbnailatorImageCompressUtil {

    /**
     * 图片格式转换
     * @param file 需要转换的文件
     * @param fileType 需要转换的文件类型 支持 jpg,png,jpeg,bmp,pnm(PBM,PGM,PPM),gif,pcx,raw
     * @param filePath 转换完成后的文件路径
     * @throws IOException 
     * outputFormat：输出的图片格式。注意使用该方法后toFile()方法不要再含有文件类型的后缀了，否则会生成 IMG_20131229_114806.jpg.jpg 的图片
     */
   public static void ImageFormatCoversion(File file,String fileType,String filePath) throws IOException{
       Thumbnails.of(file).scale(1f).outputFormat(fileType).toFile(filePath);
   }
   
   /**
    * 按比例压缩图片
    *outputQuality:输出的图片质量,范围：0.0~1.0，1为最高质量.若是输出其他格式图片,则该方法生成的图片可能比原始图片大,因为不同格式的转换大小不同,建议相同格式压缩.
    *jpg格式图片 fileType为空 默认是JPEG
    *fileType 若是输出png格式图片，则该方法作用无效
    *若png、gif格式图片中含有透明背景，使用该工具压缩处理后背景会变成黑色.,,,,1
    */
   public static void ImageScale(File file,double qulity,String fileType,String filePath) throws IOException{
       if(StringUtils.isBlank(fileType)){
	   Thumbnails.of(file).scale(1f).outputQuality(qulity).toFile(filePath);
       }else{
	   Thumbnails.of(file).scale(1f).outputQuality(qulity).outputFormat(fileType).toFile(filePath);
       }
   }
   
   
   /**
    * 按图片大小压缩图片  不保证图片的比例
    */
   public static void ImageScaleForSize(File file,int width,int height,String fileType,String filePath) throws IOException{
       if(StringUtils.isBlank(fileType)){
	   Thumbnails.of(file).forceSize(width, height).outputFormat(fileType).toFile(file);
       }else{
	   Thumbnails.of(file).forceSize(width, height).toFile(filePath);
       }
   }
   
   /**
    * 按图片大小压缩图片  保证图片的比例
    */
   public static void ImageScaleForSize2(File file,int width,int height,String fileType,String filePath)throws IOException{
       BufferedImage image  = ImageIO.read(file);
       Builder<BufferedImage> builder = null;
       
       int ImageWidth = image.getWidth();//图片真实的宽
       int ImageHeight = image.getHeight();//图片真实的高
       BigDecimal scale= new BigDecimal(width).divide(new BigDecimal(height),2,BigDecimal.ROUND_HALF_EVEN);
       BigDecimal ImageScale =new BigDecimal(ImageWidth).divide(new BigDecimal(ImageHeight),2,BigDecimal.ROUND_HALF_EVEN);
       
       if(scale.equals(ImageScale)){
	   if(ImageWidth>ImageHeight){
	       image = Thumbnails.of(file).height(height).asBufferedImage();
	   }else{
	       image = Thumbnails.of(file).width(width).asBufferedImage();
	   }
	   builder = Thumbnails.of(image).sourceRegion(Positions.CENTER,width,height).size(width,height);
       }else{
	   builder = Thumbnails.of(image).size(width,height);
       }
       builder.outputFormat(fileType).toFile(filePath);
   }
   
   /**
    * 图片旋转 
    * @param rotate 旋转角度 正数为顺时针 负数为逆时针
    * @param width 旋转完成后图片的宽
    * @param height 旋转完成后图片的高
    * 图片按90°旋转,按图片默认宽高旋转 图片会越来越模糊 图片大小会越来越小
    */
   public static void ImageRotate(File file,String filetype,int width,int height,double rotate,String filePath) throws IOException{
       if(width==0 || height==0){
	   BufferedImage image = ImageIO.read(file);
	   width = image.getWidth();
	   height = image.getHeight();
       }
       if(StringUtils.isNotBlank(filePath)){
	   Thumbnails.of(file).size(width, height).rotate(rotate).outputFormat(filetype).toFile(filePath);
       }else{
	   Thumbnails.of(file).size(width, height).rotate(rotate).toFile(filePath);
       }
   }   
   
   /**
    * 图片增加水印
    * @param glisteningPath 水印图片
    * @param positions 水印添加位置
    * @param width 图片增加水印完成后图片的宽
    * @param height 图片增加水印完成后图片的高
    */
   public static void ImageAndGlistening(File file,int width,int height,String filePath,File glisteningPath,Positions positions) throws IOException{
       if(width==0 || height==0){
	   BufferedImage image = ImageIO.read(file);
	   width = image.getWidth();
	   height = image.getHeight();
       }
       Thumbnails.of(file).size(width, height).watermark(positions,ImageIO.read(glisteningPath),0.8f).outputQuality(1f).toFile(filePath);
   }
   
   /**
    * 图片裁剪
    * @param positions 裁剪的位置
    * @param width 裁剪后图片的宽
    * @param height 裁剪后图片高
    * @param cutWidth 裁剪位置的坐标x轴
    * @param cutHeight 裁剪位置的坐标y轴
    * @param newWidth 图片裁剪完成后的图片宽
    * @param newHeight 图片裁剪完成后的图片高
    */
   public static void ImageCut(File file,Positions positions,int width,int height,int newWidth,int newHeight,int cutWidth,int cutHeight,String filePath) throws IOException{
       if(newWidth==0 || newHeight==0){
	   BufferedImage image = ImageIO.read(file);
	   newWidth = image.getWidth();
	   newHeight = image.getHeight();
       }
       if(positions!=null){
	   Thumbnails.of(file).sourceRegion(positions,width,height).size(newWidth, newHeight).keepAspectRatio(false).toFile(filePath);
       }else{
	   Thumbnails.of(file).sourceRegion(cutWidth,cutHeight,width,height).size(newWidth, newHeight).keepAspectRatio(false).toFile(filePath);
       }
   }
   
   public static void main(String[] args) throws IOException {
       File file = new File("d:\\666.jpg");
       //ThumbnailatorImageCompressUtil.ImageFormatCoversion(file,"jpeg","d:\\123555");
       //ThumbnailatorImageCompressUtil.ImageRotate(file,"jpg",440,0,180, "d:\\99999");
       //ThumbnailatorImageCompressUtil.ImageAndGlistening(file,500,300,"d:\\5555555",new File("d:\\123123.png"),Positions.CENTER);
       //ThumbnailatorImageCompressUtil.ImageScale(file,0.1,"jpg","d:\\1231231234");//默认格式为jpeg
       //ThumbnailatorImageCompressUtil.ImageScaleForSize(file, 500, 500, "jpg", "d:\\1231231234");
       ThumbnailatorImageCompressUtil.ImageScaleForSize2(file, 500, 500, "jpg", "d:\\123ds");
   }
}
