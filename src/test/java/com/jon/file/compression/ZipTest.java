package com.jon.file.compression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ZipTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private List<String> entriesOf(File zip) throws Exception {
        List<String> names = new ArrayList<>();
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zip))) {
            ZipEntry e;
            while ((e = zin.getNextEntry()) != null) {
                names.add(e.getName());
            }
        }
        return names;
    }

    private File buildTree() throws Exception {
        File dir = folder.newFolder("src");
        File deeper = new File(dir, "sub/deeper");
        assertTrue(deeper.mkdirs());
        try (FileWriter w = new FileWriter(new File(dir, "a.txt"))) {
            w.write("A");
        }
        try (FileWriter w = new FileWriter(new File(deeper, "b.txt"))) {
            w.write("B");
        }
        return dir;
    }

    @Test
    public void javaApiZipPreservesRelativePaths() throws Exception {
        File dir = buildTree();
        File zip = new File(folder.newFolder("out1"), "out.zip");

        new JavaApiFileCompressionForZIP().FileCompressionForZIP(dir.getPath(), zip.getPath());

        List<String> names = entriesOf(zip);
        assertTrue("entries: " + names, names.contains("a.txt"));
        assertTrue("entries: " + names, names.contains("sub/deeper/b.txt"));
    }

    @Test
    public void apacheZipKeepsDirectoryStructure() throws Exception {
        File dir = buildTree();
        File zip = new File(folder.newFolder("out2"), "out.zip");

        new ZipToolsForApache(zip.getPath()).fileCompress(dir.getPath());

        List<String> names = entriesOf(zip);
        // 顶层目录名作为前缀，子目录不丢层级，也不会出现重复条目
        assertTrue("entries: " + names, names.contains("src/a.txt"));
        assertTrue("entries: " + names, names.contains("src/sub/deeper/b.txt"));
        assertEquals("entries: " + names, 2, names.size());
    }

    @Test
    public void lzmaZipNotImplementedThrows() {
        try {
            FileCompressionFor7z.lzmaZip("a", "b");
            fail("unimplemented API must fail loudly");
        } catch (UnsupportedOperationException expected) {
            // 预期
        } catch (Exception e) {
            fail("unexpected exception: " + e);
        }
    }
}
