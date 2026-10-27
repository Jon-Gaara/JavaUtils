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

    /** 正在写入的目标 zip，用于排除"压缩目录里包含目标 zip 自身"的自包含 */
    private File currentZipFile;

    public File FileCompressionForZIP(String dataSource, String zipFilePath) throws IOException {
        File file = new File(dataSource);
        File zipFile = new File(zipFilePath);
        currentZipFile = zipFile;
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
                // ZIP 规范要求条目内路径用 '/' 分隔，用 File.separator 在 Windows 上会丢目录结构
                pathName = newFile.getPath().substring(basePath.length() + 1).replace(File.separatorChar, '/') + "/";
                zipOut.putNextEntry(new ZipEntry(pathName));
                zipFile(newFile, basePath, zipOut);
            } else {
                // 目标 zip 位于待压缩目录内时跳过，避免边写边读自身
                if (currentZipFile != null && newFile.getCanonicalPath().equals(currentZipFile.getCanonicalPath())) {
                    continue;
                }
                pathName = newFile.getPath();
                try (InputStream is = new FileInputStream(pathName);
                    BufferedInputStream buffer = new BufferedInputStream(is)) {
                    // 条目名保留相对目录结构并使用 '/'，否则不同目录同名文件会互相覆盖
                    String entryName = newFile.getPath().substring(basePath.length() + 1)
                        .replace(File.separatorChar, '/');
                    zipOut.putNextEntry(new ZipEntry(entryName));
                    // zipOut.setLevel(9);设置zip压缩级别 0~9
                    while ((length = buffer.read(buff)) != -1) {
                        zipOut.write(buff, 0, length);
                    }
                }
            }
        }
    }
}
