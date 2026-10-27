package com.jon.file.encryption;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Files;
import java.security.GeneralSecurityException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileEncryptionForAESTest {

    private static final byte[] KEY = "1234567887654321".getBytes();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void randomIvPerEncryption() throws Exception {
        byte[] plain = "hello-aes-中文".getBytes("UTF-8");
        byte[] c1 = FileEncryptionForAES.encodeAES(KEY, plain);
        byte[] c2 = FileEncryptionForAES.encodeAES(KEY, plain);
        assertFalse("identical ciphertext means IV is fixed", java.util.Arrays.equals(c1, c2));
        assertArrayEquals(plain, FileEncryptionForAES.decodeAES(KEY, c1));
    }

    @Test
    public void tamperedCiphertextRejected() throws Exception {
        byte[] cipher = FileEncryptionForAES.encodeAES(KEY, "data".getBytes());
        cipher[cipher.length - 1] ^= 0x01;
        try {
            FileEncryptionForAES.decodeAES(KEY, cipher);
            fail("tampered ciphertext must not decrypt");
        } catch (GeneralSecurityException expected) {
            // 预期
        }
    }

    @Test
    public void invalidKeyLengthRejected() {
        // 短密钥会被补零到 16 字节（合法）；补零后为 48 字节则不是 AES 支持的 16/24/32
        byte[] badKey = new byte[33];
        try {
            FileEncryptionForAES.encodeAES(badKey, "x".getBytes());
            fail("key must be validated");
        } catch (IllegalArgumentException expected) {
            // 预期
        } catch (Exception e) {
            fail("unexpected exception: " + e);
        }
    }

    @Test
    public void fileStreamingRoundTrip() throws Exception {
        File plain = folder.newFile("plain.txt");
        File enc = folder.newFile("enc.bin");
        File dec = folder.newFile("dec.txt");
        byte[] data = "streaming file content 中文内容".getBytes("UTF-8");
        Files.write(plain.toPath(), data);

        FileEncryptionForAES.encodeAESFile(KEY, plain.getPath(), enc.getPath());
        FileEncryptionForAES.decodeAESFile(KEY, dec.getPath(), enc.getPath());

        assertArrayEquals(data, Files.readAllBytes(dec.toPath()));
        assertTrue("encrypted file must start with 16-byte IV", enc.length() >= 16);
        assertEquals(0, (enc.length() - 16) % 16);
    }
}
