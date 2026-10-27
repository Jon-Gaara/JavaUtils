package com.jon.image;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ImageCompressTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File png;

    @Before
    public void createSourceImage() throws Exception {
        BufferedImage img = new BufferedImage(100, 80, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 100, 80);
        g.dispose();
        png = folder.newFile("src.png");
        ImageIO.write(img, "png", png);
    }

    @Test
    public void scale2SmallImageDoesNotThrowClassCastException() throws Exception {
        // 修复前 getScaledInstance 返回的 Image 被强转 BufferedImage → ClassCastException
        byte[] out = JavaApiImageCompressUtil.scale2(png.getPath(), 200, 160, false, null);
        assertNotNull(out);
        assertTrue(out.length > 0);
    }

    @Test
    public void thumbnailatorBlankFileTypeWorks() throws Exception {
        // 修复前 conditional 反转，空 fileType 时调用 outputFormat("") 抛异常
        File out = new File(folder.getRoot(), "thumb.jpg");
        ThumbnailatorImageCompressUtil.imageScale(png, 0.5, null, out.getPath());
        assertTrue(out.exists() && out.length() > 0);
    }

    @Test
    public void scaleRejectsNonPositiveRatio() {
        try {
            JavaApiImageCompressUtil.scale(png.getPath(), png.getPath() + ".out", 0, true, "png");
            fail("scale <= 0 must be rejected");
        } catch (IllegalArgumentException expected) {
            // 预期
        }
    }

    @Test
    public void thumbnailatorOutputFormatHonored() throws Exception {
        File out = new File(folder.getRoot(), "scaled.png");
        ThumbnailatorImageCompressUtil.imageScale(png, 0.5, "png", out.getPath());
        assertTrue(out.exists());
        // 关闭前读回校验格式
        assertNotNull(ImageIO.read(out));
        assertFalse(out.length() == 0);
    }
}
