package com.jon.file.resolve;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class XmlSecurityTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private static final String NORMAL_XML = "<root><name>jon</name></root>";

    private String xxeXml(File secret) {
        String path = secret.getPath().replace('\\', '/');
        return "<?xml version=\"1.0\"?><!DOCTYPE root [<!ENTITY xxe SYSTEM \"file:///" + path + "\">]>"
            + "<root><name>&xxe;</name></root>";
    }

    private File secretFile() throws Exception {
        File secret = folder.newFile("secret.txt");
        try (FileWriter w = new FileWriter(secret)) {
            w.write("TOPSECRET");
        }
        return secret;
    }

    @Test
    public void dom4jParsesNormalXml() throws Exception {
        Dom4jUtils utils = new Dom4jUtils();
        utils.getXMLDocument(NORMAL_XML);
        assertEquals("jon", utils.getDocument().getRootElement().element("name").getText());
    }

    @Test
    public void dom4jRejectsXxe() throws Exception {
        File payload = folder.newFile("xxe.xml");
        try (FileWriter w = new FileWriter(payload)) {
            w.write(xxeXml(secretFile()));
        }
        try {
            new Dom4jUtils().getXMLDocumentForFile(payload.getPath());
            fail("XXE payload must be rejected");
        } catch (Exception expected) {
            // 预期：DOCTYPE 被禁用
        }
    }

    @Test
    public void jdomParsesNormalXml() throws Exception {
        Map<String, String> parsed = JDOMUtils.doXMLParse(NORMAL_XML);
        assertNotNull(parsed);
        assertEquals("jon", parsed.get("name"));
    }

    @Test
    public void jdomRejectsXxe() throws Exception {
        try {
            JDOMUtils.doXMLParse(xxeXml(secretFile()));
            fail("XXE payload must be rejected");
        } catch (Exception expected) {
            // 预期：DOCTYPE 被禁用
        }
    }

    @Test
    public void propertiesMissingKeyFailsFast() {
        try {
            PropertiesUtils.getPropertiesValue("no.such.key");
            fail("missing key must throw instead of returning null");
        } catch (IllegalStateException expected) {
            assertTrue(expected.getMessage().contains("no.such.key"));
        }
    }
}
