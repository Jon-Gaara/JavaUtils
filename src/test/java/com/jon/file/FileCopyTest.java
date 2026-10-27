package com.jon.file;

import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.nio.file.Files;
import java.util.Random;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileCopyTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void bufferCopyIsByteIdenticalForOddSize() throws Exception {
        File src = folder.newFile("src.bin");
        File dst = folder.newFile("dst.bin");
        // 非缓冲区整数倍，覆盖"最后一段短读"场景
        byte[] data = new byte[100003];
        new Random(42).nextBytes(data);
        Files.write(src.toPath(), data);

        FileCopy.fileCopyBuffer(src, dst);

        assertArrayEquals(data, Files.readAllBytes(dst.toPath()));
    }
}
