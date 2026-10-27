package com.jon.file.compression;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 用org.apache.tools.zip.ZipOutputStream来 进行ZIP文件压缩
 */
public class ZipToolsForApache {
    private static final int BUFFER = 8192;
    private File zipFile;

    public ZipToolsForApache(String pathName) {
        zipFile = new File(pathName);
    }

    public void fileCompress(String srcPathName) {
        File file = new File(srcPathName);
        if (!file.exists()) {
            throw new RuntimeException(srcPathName + "不存在");
        }
        try (FileOutputStream fileOut = new FileOutputStream(zipFile);
            CheckedOutputStream checkOut = new CheckedOutputStream(fileOut, new CRC32());
            ZipOutputStream zipOut = new ZipOutputStream(checkOut)) {
            String baseDir = "";
            fileCompress(file, zipOut, baseDir);
            zipOut.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fileCompress(File file, ZipOutputStream zipOut, String baseDir) {
		// 判断是文件还是目录
        if (file.isDirectory()) {
            this.compressDirectory(file, zipOut, baseDir);
        } else {
            this.compressFile(file, zipOut, baseDir);
        }
    }

    /**
     * 压缩目录
     */
    private void compressDirectory(File file, ZipOutputStream zipOut, String baseDir) {
        if (!file.exists()) {
            return;
        }
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
			// 递归，保留目录层级：否则不同子目录下的同名文件会产生重复的 ZipEntry
            for (int i = 0; i < files.length; i++) {
                fileCompress(files[i], zipOut, baseDir + file.getName() + "/");
            }
        }
    }

    /**
     * 压缩文件
     */
    private void compressFile(File file, ZipOutputStream zipOut, String baseDir) {
        if (!file.exists()) {
            return;
        }
        try {
            // 目标 zip 本身位于待压缩目录内时跳过，否则会把正在写的归档读进来
            if (file.getCanonicalPath().equals(zipFile.getCanonicalPath())) {
                return;
            }
        } catch (Exception ignored) {
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            ZipEntry zip = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(zip);
            int count;
            byte[] data = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
