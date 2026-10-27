package com.jon.file.compression;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

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
		throw new UnsupportedOperationException("lzmaZip 尚未实现");
	}
	
	public static void lzmaZip2(String fileName,String newFilePath) throws Exception{
		throw new UnsupportedOperationException("lzmaZip2 尚未实现");
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
					// Zip Slip 防护 + 目录创建必须在 extractSlow 之前完成
					File targetFile = null;
					try {
						File targetDir = new File(newFilePath).getCanonicalFile();
						targetFile = new File(targetDir, item.getPath()).getCanonicalFile();
						if (!targetFile.getPath().startsWith(targetDir.getPath() + File.separator)) {
							throw new IOException("非法归档条目路径: " + item.getPath());
						}
						File parent = targetFile.getParentFile();
						if (parent != null && !parent.exists()) {
							parent.mkdirs();
						}
					} catch (IOException e) {
						System.err.println("跳过条目 " + item.getPath() + ": " + e.getMessage());
						targetFile = null;
					}
					if (targetFile == null) {
						continue;
					}
					// 整个条目只开一个输出流：extractSlow 会按 chunk 多次回调，每次重开(截断)只会留下最后一块
					try (FileOutputStream fos = new FileOutputStream(targetFile)) {
						result = item.extractSlow(data -> {
							try {
								fos.write(data);
							} catch (IOException e) {
								e.printStackTrace();
							}
							// Consume data
							hash[0] ^= Arrays.hashCode(data);
							sizeArray[0] += data.length;
							//Return amount of consumed data
							return data.length;
						});
					}
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
			// 工具类不能终止宿主 JVM，交给调用方处理
			throw new IllegalStateException("7z 解压失败: " + filePath, e);
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