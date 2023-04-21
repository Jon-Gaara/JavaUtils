package com.jon.file.compression;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.google.zxing.aztec.decoder.Decoder;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;


public class FileCompressionFor7z {
	
	/**
	 * 7Z压缩
	 * @param fileName 源文件
	 * @param newFilePath 新文件路径
	 * @throws Exception 
	 */
	public static void lzmaZip(String fileName,String newFilePath) throws Exception{
		/*File file = new File(fileName);
		File newFile = new File(newFilePath);
		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(newFile));
		boolean eos = true;
		SevenZip.Compression.LZMA.Encoder encoder = new Encoder();
		encoder.SetEndMarkerMode(eos);
		encoder.WriteCoderProperties(outStream);
		long fileSize = fileName.length();
		if (eos){
			fileSize = -1;
		};
		for (int i = 0; i < 8; i++) {
			outStream.write((int) (fileSize >>> (8 * i)) & 0xFF);
		};
		encoder.Code(inStream, outStream, -1, -1, null);*/
	}
	
	public static void lzmaZip2(String fileName,String newFilePath) throws Exception{
		/*File file = new File(fileName);
		File newFile = new File(newFilePath);
		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(newFile));
		int propertiesSize = 5;
		byte[] fileByte = new byte[propertiesSize];
		if(inStream.read(fileByte, 0, propertiesSize)!=propertiesSize){
			throw new Exception("input .lzma file is to short");
		}
		Decoder decode = new Decoder();
		if(!decode.SetDecoderProperties(fileByte)){
			throw new Exception("Incorrect stream properties");
		};
		long outSize = 0;
		for(int i=0;i<0;i++){
			int v = inStream.read();
			if(v<0) throw new Exception("Can't read stream size");
			outSize |=((long)v) << (8*i);
		}
		if(!decode.Code(inStream, outStream, outSize)){
			throw new Exception("Error in data stream");
		}
		outStream.flush();
		outStream.close();
		inStream.close();*/
	}
	
	/**
	 * 7Z解压
	 * @param filePath 源文件
	 * @param newFilePath 新文件路径
	 */
	public static void unzipFor7z(String filePath,final String newFilePath){
		RandomAccessFile randomAccessFile = null;
		IInArchive inArchive = null;
		try {
			randomAccessFile = new RandomAccessFile(filePath, "r");
			// autodetect archive type
			inArchive =  SevenZip.openInArchive(null,
					new RandomAccessFileInStream(randomAccessFile));
			// Getting simple interface of the archive inArchive
			ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

			System.out.println("  Hash  |  Size  | Filename");
			System.out.println("----------+------------+---------");

			for (final ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
				final int[] hash = new int[] { 0 };
				if (!item.isFolder()) {
					ExtractOperationResult result;
					final long[] sizeArray = new long[1];
					result = item.extractSlow(data -> {
						// Write to file
						FileOutputStream fos;
						try {
							File file = new File(newFilePath+File.separator+item.getPath());
							// error occours below
							// file.getParentFile().mkdirs();
							fos = new FileOutputStream(file);
							fos.write(data);
							fos.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						// Consume data
						hash[0] ^= Arrays.hashCode(data);
						sizeArray[0] += data.length;
						//Return amount of consumed data
						return data.length;
					});
					if (result == ExtractOperationResult.OK) {
						System.out.println(String.format("%9X | %10s | %s",
								hash[0], sizeArray[0], item.getPath()));
					} else {
						System.err.println("Error extracting item: " + result);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error occurs: " + e);
			e.printStackTrace();
			System.exit(1);
		} finally {
			if (inArchive != null) {
				try {
					inArchive.close();
				} catch (SevenZipException e) {
					System.err.println("Error closing archive: " + e);
				}
			}
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					System.err.println("Error closing file: " + e);
				}
			}
		}
	}
}