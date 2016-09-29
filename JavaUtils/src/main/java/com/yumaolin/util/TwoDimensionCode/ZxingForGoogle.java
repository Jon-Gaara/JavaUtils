package com.yumaolin.util.TwoDimensionCode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
public class ZxingForGoogle {
	private static final String  charset = "utf-8";
	//生成二维码
	public void CreateParseCode(String path){
		String text="你是牛啊！";
		int width=300;
		int height=300;
		String format="png";//二维码图片格式
		Map<EncodeHintType,String> hints = new HashMap<EncodeHintType,String>();//设置二维码参数
		hints.put(EncodeHintType.CHARACTER_SET, charset);//设置编码格式
		try {
		    BitMatrix bmt = new MultiFormatWriter().encode(text,BarcodeFormat.QR_CODE, width, height,hints);
		    //生成二维码
		    File outputFile = new File(path);
		    MatrixToImageWriter.writeToFile(bmt, format, outputFile);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
    /** 
     * 二维码的解析 
     * @param <T>
     * 
     * @param file 
     */  
    public  void parseCode(File file){  
        try{  
            MultiFormatReader formatReader = new MultiFormatReader();  
            if (!file.exists()){
                return;  
            }  
            BufferedImage image = ImageIO.read(file);  
            LuminanceSource source = new BufferedImageLuminanceSource(image);  
            Binarizer binarizer = new HybridBinarizer(source);  
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);  
   
            Map<DecodeHintType,String> hints = new HashMap<DecodeHintType,String>();  
            hints.put(DecodeHintType.CHARACTER_SET, charset);  
            Result result = formatReader.decode(binaryBitmap,hints);  
            System.out.println("解析结果 = " + result.toString());  
            System.out.println("二维码格式类型 = " + result.getBarcodeFormat());  
            System.out.println("二维码文本内容 = " + result.getText());  
        }catch (Exception e)  {  
            e.printStackTrace();  
        }  
    }  
	
	public static void main(String[] args) {
	    ZxingForGoogle zfg = new ZxingForGoogle();
	    zfg.CreateParseCode("D://test.png");
	    zfg.parseCode(new File("D://test.png"));
	}
}