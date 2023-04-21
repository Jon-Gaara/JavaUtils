package com.jon.file.compression;

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

/**
 * Java api 进行ZIP文件压缩,文件（夹）名称带中文时，出现乱码问题
 */
public class JavaApiFileCompressionForZIP {

    public File FileCompressionForZIP(String dataSource, String zipFilePath) throws IOException {
        File file = new File(dataSource);
        File zipFile = new File(zipFilePath);
        try (OutputStream out = new FileOutputStream(zipFile); BufferedOutputStream buf = new BufferedOutputStream(out);
            // jdk1.7支持
            ZipOutputStream zipOut = new ZipOutputStream(buf, StandardCharsets.UTF_8)) {

            String basePath;
            // 获取目录
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }
            zipFile(file, basePath, zipOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    private void zipFile(File file, String basePath, ZipOutputStream zipOut) throws IOException {
        File[] files;
        if (file.isDirectory()) {
            files = file.listFiles();
        } else {
            files = new File[1];
            files[0] = file;
        }
        String pathName;
        byte[] buff = new byte[1024];
        int length;
        if (files == null) {
            return;
        }
        for (File newFile : files) {
            if (newFile.isDirectory()) {
                // 不带目录压缩
                pathName = newFile.getPath().substring(basePath.length() + 1) + File.separator;
                zipOut.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zipOut);
            } else {
                pathName = newFile.getPath();
                try (InputStream is = new FileInputStream(pathName);
                    BufferedInputStream buffer = new BufferedInputStream(is)) {
                    // zipOut.putNextEntry(new ZipEntry(pathName));带目录压缩
                    // 不带目录压缩
                    zipOut.putNextEntry(new ZipEntry(newFile.getName()));
                    // zipOut.setLevel(9);设置zip压缩级别 0~9
                    while ((length = buffer.read(buff)) != -1) {
                        zipOut.write(buff, 0, length);
                    }
                }
            }
        }
    }
}
