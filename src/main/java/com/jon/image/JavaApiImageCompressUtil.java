package com.jon.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.lang.StringUtils;

/**
 * 图片处理工具类：<br>
 * 功能：缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等 http://blog.csdn.net/cenlop/article/details/75184 使用JAI扩展
 */
public class JavaApiImageCompressUtil {
    /**
     * 几种常见的图片格式
     */
    public static String IMAGE_TYPE_GIF = "gif";
    public static String IMAGE_TYPE_JPG = "jpg";
    public static String IMAGE_TYPE_JPEG = "jpeg";
    public static String IMAGE_TYPE_BMP = "bmp";
    public static String IMAGE_TYPE_PNG = "png";
    public static String IMAGE_TYPE_PSD = "psd";

    /**
     * 缩放图像（按比例缩放）
     * 
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param scale 缩放比例
     * @param flag 缩放选择:true 放大; false 缩小;
     */
    public static void scale(String srcImageFile, String result, int scale, boolean flag, String format) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            int width = src.getWidth();
            int height = src.getHeight();
            // 放大
            if (flag) {
                width = width * scale;
                height = height * scale;
            } else {
                // 缩小
                width = width / scale;
                height = height / scale;
            }
            /**
             * image.SCALE_SMOOTH //平滑优先 image.SCALE_FAST//速度优先 image.SCALE_AREA_AVERAGING //区域均值 image.SCALE_REPLICATE
             * //像素复制型缩放 image.SCALE_DEFAULT //默认缩放模式
             */
            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            if (StringUtils.isBlank(format)) {
                ImageIO.write(tag, "JPEG", new File(result));
            } else {
                ImageIO.write(tag, format, new File(result));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缩放图像（按比例缩放）
     * 
     * @param scale [0.0, 1.0] 该方法失真严重 谨慎使用
     */
    public static void scale2(String srcImageFile, String result, float scale, String format) {
        RenderedImage rendImage;
        try {
            rendImage = ImageIO.read(new File(srcImageFile));
            ImageWriter imgWriter;
            if (StringUtils.isBlank(format)) {
                imgWriter = ImageIO.getImageWritersByFormatName(format).next();
            } else {
                imgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            }
            // 准备输出文件
            ImageOutputStream ios = ImageIO.createImageOutputStream(new File(result));
            imgWriter.setOutput(ios);

            // 设置压缩比
            ImageWriteParam imgWriteParam = imgWriter.getDefaultWriteParam();
            if (imgWriteParam.canWriteCompressed()) {
                imgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                imgWriteParam.setCompressionQuality(scale);
            }

            // 生成图片
            imgWriter.write(null, new IIOImage(rendImage, null, null), imgWriteParam);

            ios.flush();
            imgWriter.dispose();
            ios.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缩放图像（按高度和宽度缩放）
     * 
     * @param srcImageFile 源图像文件地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     */
    public static byte[] scale2(String srcImageFile, int height, int width, boolean bb, String format) {
        try {
            // 缩放比例
            double ratio = 0.0;
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            // 补白
            if (bb) {
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null)) {
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
                        itemp.getHeight(null), Color.white, null);
                } else {
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
                        itemp.getHeight(null), Color.white, null);
                }
                g.dispose();
                itemp = image;
            }
            // ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if (StringUtils.isBlank(format)) {
                ImageIO.write((BufferedImage)itemp, "JPEG", out);
            } else {
                ImageIO.write((BufferedImage)itemp, format, out);
            }
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 缩放图像（按高度和宽度缩放）
     * 
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     */
    public static void scale2(String srcImageFile, String result, int height, int width, boolean bb, String format) {
        try {
            // 缩放比例
            double ratio;
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            // 补白
            if (bb) {
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null)) {
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
                        itemp.getHeight(null), Color.white, null);
                } else {
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
                        itemp.getHeight(null), Color.white, null);
                }
                g.dispose();
                itemp = image;
            }

            if (StringUtils.isBlank(format)) {
                ImageIO.write((BufferedImage)itemp, "JPEG", new File(result));
            } else {
                ImageIO.write((BufferedImage)itemp, format, new File(result));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割(按指定起点坐标和宽高切割)
     * 
     * @param srcImageFile 源图像地址
     * @param result 切片后的图像地址
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    public static void cut(String srcImageFile, String result, int x, int y, int width, int height, String format) {
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            // 源图宽度
            int srcWidth = bi.getHeight();
            // 源图高度
            int srcHeight = bi.getWidth();
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img =
                    Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                // 绘制切割后的图
                g.drawImage(img, 0, 0, width, height, null);
                g.dispose();
                // 输出为文件
                if (StringUtils.isBlank(format)) {
                    ImageIO.write(tag, "JPEG", new File(result));
                } else {
                    ImageIO.write(tag, format, new File(result));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割（指定切片的行数和列数）
     * 
     * @param srcImageFile 源图像地址
     * @param descDir 切片目标文件夹
     * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
     * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
     */
    public static void cut2(String srcImageFile, String descDir, int rows, int cols, String format) {
        try {
            if (rows <= 0 || rows > 20) {
                // 切片行数
                rows = 2;
            }
            if (cols <= 0 || cols > 20) {
                // 切片列数
                cols = 2;
            }
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            // 源图宽度
            int srcWidth = bi.getHeight();
            // 源图高度
            int srcHeight = bi.getWidth();
            if (srcWidth > 0 && srcHeight > 0) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                // 每张切片的宽度
                int destWidth;
                // 每张切片的高度
                int destHeight;
                // 计算切片的宽度和高度
                if (srcWidth % cols == 0) {
                    destWidth = srcWidth / cols;
                } else {
                    destWidth = (int)Math.floor((double)srcWidth / cols) + 1;
                }
                if (srcHeight % rows == 0) {
                    destHeight = srcHeight / rows;
                } else {
                    destHeight = (int)Math.floor((double)srcWidth / rows) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit()
                            .createImage(new FilteredImageSource(image.getSource(), cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        // 绘制缩小后的图
                        g.drawImage(img, 0, 0, null);
                        g.dispose();
                        // 输出为文件
                        if (StringUtils.isBlank(format)) {
                            ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
                        } else {
                            ImageIO.write(tag, format, new File(descDir + "_r" + i + "_c" + j + "." + format));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割（指定切片的宽度和高度）
     * 
     * @param srcImageFile 源图像地址
     * @param descDir 切片目标文件夹
     * @param destWidth 目标切片宽度。默认200
     * @param destHeight 目标切片高度。默认150
     */
    public static void cut3(String srcImageFile, String descDir, int destWidth, int destHeight, String format) {
        try {
            // 切片宽度
            if (destWidth <= 0) {
                destWidth = 200;
            }
            // 切片高度
            if (destHeight <= 0) {
                destHeight = 150;
            }
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            // 源图宽度
            int srcWidth = bi.getHeight();
            // 源图高度
            int srcHeight = bi.getWidth();
            if (srcWidth > destWidth && srcHeight > destHeight) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                // 切片横向数量
                int cols;
                // 切片纵向数量
                int rows;
                // 计算切片的横向和纵向数量
                if (srcWidth % destWidth == 0) {
                    cols = srcWidth / destWidth;
                } else {
                    cols = (int)Math.floor((double)srcWidth / destWidth) + 1;
                }
                if (srcHeight % destHeight == 0) {
                    rows = srcHeight / destHeight;
                } else {
                    rows = (int)Math.floor((double)srcHeight / destHeight) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit()
                            .createImage(new FilteredImageSource(image.getSource(), cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        if (StringUtils.isBlank(format)) {
                            ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
                        } else {
                            ImageIO.write(tag, format, new File(descDir + "_r" + i + "_c" + j + "." + format));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
     * 
     * @param srcImageFile 源图像地址
     * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
     * @param destImageFile 目标图像地址
     */
    public static void convert(String srcImageFile, String formatName, String destImageFile) {
        try {
            File f = new File(srcImageFile);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 彩色转为黑白
     * 
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     */
    public final static void gray(String srcImageFile, String destImageFile, String format) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            if (StringUtils.isBlank(format)) {
                ImageIO.write(src, "JPEG", new File(destImageFile));
            } else {
                ImageIO.write(src, format, new File(destImageFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加文字水印
     * 
     * @param pressText 水印文字
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param fontName 水印的字体名称
     * @param fontStyle 水印的字体样式
     * @param color 水印的字体颜色
     * @param fontSize 水印的字体大小
     * @param x 修正值
     * @param y 修正值
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public static void pressText(String pressText, String srcImageFile, String destImageFile, String fontName,
        int fontStyle, Color color, int fontSize, int x, int y, float alpha, String format) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            if (StringUtils.isBlank(format)) {
                ImageIO.write(image, "JPEG", new File(destImageFile));
            } else {
                ImageIO.write(image, format, new File(destImageFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加图片水印
     * 
     * @param pressImg 水印图片
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param x 修正值。 默认在中间
     * @param y 修正值。 默认在中间
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public static void pressImage(String pressImg, String srcImageFile, String destImageFile, int x, int y, float alpha,
        String format) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image bSrc = ImageIO.read(new File(pressImg));
            int bWideth = bSrc.getWidth(null);
            int bHeight = bSrc.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(bSrc, (wideth - bWideth) / 2, (height - bHeight) / 2, bWideth, bHeight, null);
            // 水印文件结束
            g.dispose();
            if (StringUtils.isBlank(format)) {
                ImageIO.write(image, "JPEG", new File(destImageFile));
            } else {
                ImageIO.write(image, format, new File(destImageFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算text的长度（一个中文算两个字符）
     * 
     * @param text
     * @return
     */
    public final static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if ((text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }

    // /**
    // * 程序入口：用于测试
    // * @param args
    // */
    public static void main(String[] args) {
        // 1-缩放图像：
        // 方法一：按比例缩放
        // JavaApiImageCompressUtil.scale("d:/111.jpg", "d:/111_scale.jpg",1, false,"jpeg");//测试OK
        JavaApiImageCompressUtil.scale2("d:/1476412539888.png", "d:/111_scale.png", 0.6f, "png");
        // 方法二：按高度和宽度缩放
        // JavaApiImageCompressUtil.scale2("e:/abc.jpg", "e:/abc_scale2.jpg", 500, 300, true);//测试OK
        // 2-切割图像：
        // 方法一：按指定起点坐标和宽高切割
        // JavaApiImageCompressUtil.cut("e:/abc.jpg", "e:/abc_cut.jpg", 0, 0, 400, 400 );//测试OK
        // 方法二：指定切片的行数和列数
        // JavaApiImageCompressUtil.cut2("e:/abc.jpg", "e:/", 2, 2 );//测试OK
        // 方法三：指定切片的宽度和高度
        // JavaApiImageCompressUtil.cut3("e:/abc.jpg", "e:/", 300, 300 );//测试OK
        // 3-图像类型转换：
        // JavaApiImageCompressUtil.convert("e:/abc.jpg", "GIF", "e:/abc_convert.gif");//测试OK
        // 4-彩色转黑白：
        // JavaApiImageCompressUtil.gray("e:/abc.jpg", "e:/abc_gray.jpg");//测试OK
        // 5-给图片添加文字水印：
        // 方法一：
        // JavaApiImageCompressUtil.pressText("我是水印文字","e:/abc.jpg","e:/abc_pressText.jpg","宋体",Font.BOLD,Color.white,80,
        // 0, 0, 0.5f);//测试OK
        // 方法二：
        // JavaApiImageCompressUtil.pressText2("我也是水印文字", "e:/abc.jpg","e:/abc_pressText2.jpg", "黑体", 36, Color.white,
        // 80, 0, 0, 0.5f);//测试OK
        // 6-给图片添加图片水印：
        // JavaApiImageCompressUtil.pressImage("e:/abc2.jpg", "e:/abc.jpg","e:/abc_pressImage.jpg", 0, 0, 0.5f);//测试OK
    }
}