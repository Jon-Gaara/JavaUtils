package com.jon.file.resolve;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.CRC32;

public class MemoryMapUtils {

    /**
     * 顺序读写校验
     */
    public static long checksumInputStream(Path filename) throws IOException {
        try (InputStream in = Files.newInputStream(filename)) {
            CRC32 crc = new CRC32();
            int c;
            while ((c = in.read()) != -1) {
                crc.update(c);
            }
            return crc.getValue();
        }
    }

    /**
     * 缓存读写校验
     */
    public static long checksumBufferedInputStream(Path filename) throws IOException {
        try (InputStream in = new BufferedInputStream(Files.newInputStream(filename))) {
            CRC32 crc = new CRC32();
            int c;
            while ((c = in.read()) != -1) {
                crc.update(c);
            }
            return crc.getValue();
        }
    }

    /**
     * 随机读写校验
     */
    public static long checksumRandomAccessFile(Path filename) throws IOException {
        try (RandomAccessFile in = new RandomAccessFile(filename.toFile(), "r")) {
            long length = in.length();
            CRC32 crc = new CRC32();
            for (long p = 0; p < length; p++) {
                in.seek(p);
                int c = in.readByte();
                crc.update(c);
            }
            return crc.getValue();
        }
    }

    /**
     * 内存映射读写校验
     */
    public static long checksumMappedFile(Path filename) throws IOException {
        try (FileChannel channel = FileChannel.open(filename)) {
            CRC32 crc = new CRC32();
            int length = (int)channel.size();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
            for (int p = 0; p < length; p++) {
                int c = buffer.get(p);
                crc.update(c);
            }
            return crc.getValue();
        }
    }

    public static void main(String[] args) throws IOException {
        // 读写速度 checksumMappedFile>checksumBufferedInputStream>checksumInputStream>checksumRandomAccessFile
        System.out.println("Input Stream:");
        long start = System.currentTimeMillis();
        Path fileName = Paths.get("d:/zlpay.war");
        long crcValue = checksumInputStream(fileName);
        long end = System.currentTimeMillis();
        System.out.println(Long.toHexString(crcValue));
        System.out.println((end - start) + " milliseconds");

        System.out.println("Buffered Input Stream:");
        start = System.currentTimeMillis();
        crcValue = checksumBufferedInputStream(fileName);
        end = System.currentTimeMillis();
        System.out.println(Long.toHexString(crcValue));
        System.out.println((end - start) + " milliseconds");

        System.out.println("Random Access File:");
        start = System.currentTimeMillis();
        crcValue = checksumRandomAccessFile(fileName);
        end = System.currentTimeMillis();
        System.out.println(Long.toHexString(crcValue));
        System.out.println((end - start) + " milliseconds");

        System.out.println("Mapped File:");
        start = System.currentTimeMillis();
        crcValue = checksumMappedFile(fileName);
        end = System.currentTimeMillis();
        System.out.println(Long.toHexString(crcValue));
        System.out.println((end - start) + " milliseconds");
    }
}
