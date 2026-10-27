package com.jon.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class EncryptionTest {

    @Test
    public void md5MatchesStandardDigest() {
        // printf 'test' | md5sum
        assertEquals("098f6bcd4621d373cade4e832627b4f6", MD5Encryption.getMD5("test", "MD5", "utf-8"));
    }

    @Test
    public void md5DiffersPerInput() {
        assertNotEquals(MD5Encryption.getMD5("a", "MD5", "utf-8"), MD5Encryption.getMD5("b", "MD5", "utf-8"));
    }

    @Test(expected = IllegalStateException.class)
    public void md5InvalidAlgorithmFailsFast() {
        MD5Encryption.getMD5("x", "NOT_A_ALGO", "utf-8");
    }

    @Test
    public void base64IsSingleLineAndRoundTrips() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            sb.append('a');
        }
        String longText = sb.toString();
        String encoded = Base64Encryption.encode(longText.getBytes("UTF-8"));
        assertFalse("chunked base64 must not contain line breaks", encoded.contains("\r") || encoded.contains("\n"));
        assertEquals(longText, new String(Base64Encryption.decode(encoded), "UTF-8"));
    }

    @Test
    public void desRoundTripAndRandomIv() {
        System.setProperty("des.key", "unit-test-secret");
        try {
            String enc1 = DESUtil.getEncString("hello-中文-123");
            String enc2 = DESUtil.getEncString("hello-中文-123");
            assertEquals("hello-中文-123", DESUtil.getDesString(enc1));
            assertNotEquals("GCM must use a random IV each time", enc1, enc2);
        } finally {
            System.clearProperty("des.key");
        }
    }
}
