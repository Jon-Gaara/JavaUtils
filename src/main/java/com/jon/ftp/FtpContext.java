package com.jon.ftp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpContext {

    private static String encoding = System.getProperty("file.encoding");
    private static String url = System.getenv().getOrDefault("FTP_HOST", "127.0.0.1");
    private static int port = Integer.parseInt(System.getenv().getOrDefault("FTP_PORT", "21"));
    private static String username = System.getenv("FTP_USERNAME");
    private static String password = System.getenv("FTP_PASSWORD");
    private static String localFTPPath = "D:/";

    /**
     * 每次调用创建独立连接（静态共享 client 会被并发调用互相断开）
     */
    private static FTPClient connect() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(encoding);
        if (port == 21) {
            ftpClient.connect(url);
        } else {
            ftpClient.connect(url, port);
        }
        ftpClient.login(username, password);
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("FTP server refused connection, reply=" + reply);
        }
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        // 被动模式对 NAT/防火墙更友好；主动模式需要服务器回连客户端
        ftpClient.enterLocalPassiveMode();
        return ftpClient;
    }

    private static void disconnect(FTPClient ftpClient) {
        if (ftpClient == null || !ftpClient.isConnected()) {
            return;
        }
        try {
            ftpClient.logout();
        } catch (IOException ignored) {
        }
        try {
            ftpClient.disconnect();
        } catch (IOException ignored) {
        }
    }

    /**
     * Description: 向FTP服务器上传文件
     * 
     * @param path FTP服务器保存目录,如果是根目录则为“/”
     * @param filename 上传到FTP服务器上的文件名
     * @param input 本地文件输入流
     * @return 成功返回true，否则返回false
     * @throws IOException
     */
    public static boolean uploadFile(String path, String filename, InputStream input) throws IOException {
        boolean result = false;
        FTPClient ftpClient = connect();
        try {
            // 转移工作目录至指定目录下
            boolean change = ftpClient.changeWorkingDirectory(path);
            if (change) {
                result = ftpClient.storeFile(
                    new String(filename.getBytes(encoding), StandardCharsets.ISO_8859_1), input);
                if (result) {
                    System.out.println("上传成功!");
                }
            }
            input.close();
        } finally {
            disconnect(ftpClient);
        }
        return result;
    }

    /**
     * Description: 从FTP服务器下载文件
     * 
     * @param remotePath FTP服务器上的相对路径
     * @param fileName 要下载的文件名
     * @param localPath 下载后保存到本地的路径
     * @return
     * @throws IOException
     */
    public static boolean downFile(String remotePath, String fileName, String localPath) throws IOException {
        boolean result = false;
        FTPClient ftpClient = connect();
        try {
            // 转移到FTP服务器目录至指定的目录下
            ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), StandardCharsets.ISO_8859_1));
            // 本地目标目录不存在时先创建调用方指定的 localPath（原实现只创建了硬编码的 localFTPPath）
            File localDir = new File(localPath);
            if (!localDir.exists()) {
                localDir.mkdirs();
            }
            // 获取文件列表
            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ff : fs) {
                if (ff == null) {
                    continue;
                }
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localDir, ff.getName());
                    try (OutputStream is = new FileOutputStream(localFile)) {
                        // 传播真实结果，传输被拒时不能报成功
                        result = ftpClient.retrieveFile(fileName, is);
                    }
                }
            }
        } finally {
            disconnect(ftpClient);
        }
        return result;
    }

    public static List<String> readFile(String remotePath, String fileName) {
        List<String> result = new ArrayList<>();
        FTPClient ftpClient = null;
        try {
            ftpClient = connect();
            // 转移到FTP服务器目录至指定的目录下
            ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), StandardCharsets.ISO_8859_1));
            // 获取文件列表
            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ff : fs) {
                if (ff == null) {
                    continue;
                }
                File file = new File(localFTPPath);
                if (!file.exists()) {
                    file.mkdir();
                }
                if (ff.getName().contains(fileName) && !ff.getName().contains("H")) {
                    File localFile = new File(localFTPPath + File.separator + ff.getName());
                    // 每个文件独立的行缓冲，避免把上一个文件的内容拼进当前文件
                    List<String> fileLines = new ArrayList<>();
                    InputStream in = ftpClient.retrieveFileStream(ff.getName());
                    if (in == null) {
                        // 服务器拒绝传输或数据连接打不开时返回 null，继续读会 NPE 且控制连接错位
                        System.err.println("无法获取文件流: " + ff.getName());
                        continue;
                    }
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                        String tempString;
                        while ((tempString = reader.readLine()) != null) {
                            fileLines.add(tempString);
                        }
                    } finally {
                        // 必须调用，否则控制连接状态不一致
                        ftpClient.completePendingCommand();
                    }
                    if (!localFile.exists()) {
                        localFile.createNewFile();
                    }
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(localFile))) {
                        for (String str : fileLines) {
                            bw.write(str + System.getProperty("line.separator"));
                        }
                    }
                    result.addAll(fileLines);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect(ftpClient);
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            System.out.println(encoding);
            FileInputStream input = new FileInputStream("D:/周星驰自行车自行车自行车.txt");
            FtpContext.uploadFile("/", "周星驰自行车自行车自行车.txt", input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
