package com.jon.file.resolve;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class IOReaderUtils {

    /**
     * 写入固定长度的字符串 不足补0
     * 
     * @throws IOException
     */
    public static void writeFixedString(String s, int size, DataOutput out) throws IOException {
        for (int i = 0; i < size; i++) {
            char ch = 0;
            if (i < s.length())
                ch = s.charAt(i);
            out.writeChar(ch);
        }
    }

    /**
     * 读取固定长度的字符串 不足补0
     * 
     * @throws IOException
     */
    public static String readFixedString(DataInput in, int size) throws IOException {
        StringBuilder sbuilder = new StringBuilder(size);
        int i = 0;
        boolean more = true;
        while (more && i < size) {
            char ch = in.readChar();
            i++;
            if (ch == 0) {
                more = false;
            } else {
                sbuilder.append(ch);
            }
        }
        in.skipBytes(2 * (size - i));
        return sbuilder.toString();
    }

    /**
     * 遍历当前目录所有子目录
     * 
     * @throws IOException
     */
    public static void FileIterator(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                if (attrs.isDirectory()) {
                    System.out.println(path);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
			public FileVisitResult visitFileFailed(Path path, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 遍历zip文档里的所有文件
     * 
     * @throws IOException
     */
    public static void ZipFileIterator(Path path) throws IOException {
        FileSystem fileSystem = FileSystems.newFileSystem(path, null);
        Files.walkFileTree(fileSystem.getPath("/"), new SimpleFileVisitor<Path>() {
            @Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                System.out.println(path);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void main(String[] args) throws FileNotFoundException {
        /**
         * r只读 rw读写 rws表示每次更新时,都对数据和元数据的写磁盘操作进行同步的读/写模式 rwd表示每次更新时,只对数据的写磁盘操作进行同步的读/写模式
         */
        // RandomAccessFile raf = new RandomAccessFile(new File(""),"rw");
    }
}
