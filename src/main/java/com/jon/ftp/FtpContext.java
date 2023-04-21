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

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpContext {

    private static FTPClient ftpClient = new FTPClient();
    private static String encoding = System.getProperty("file.encoding");
    private static String url = "127.0.0.1";
    private static int port = 21;
    private static String username = "FTPTest";
    private static String password = "123456";
    private static String localFTPPath = "D:/";

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
        int reply;
        try {
            ftpClient.connect(url, port);
            // 登录
            ftpClient.login(username, password);
            ftpClient.setControlEncoding(encoding);
            // 检验是否连接成功
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("连接失败");
                ftpClient.disconnect();
                return result;
            }

            // 转移工作目录至指定目录下
            boolean change = ftpClient.changeWorkingDirectory(path);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (change) {
                result = ftpClient.storeFile(new String(filename.getBytes(encoding), StandardCharsets.ISO_8859_1), input);
                if (result) {
                    System.out.println("上传成功!");
                }
            }
            input.close();
            ftpClient.logout();
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
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
        boolean result;
        OutputStream is;
        try {
            int reply;
            ftpClient.setControlEncoding(encoding);
            /*
             *  为了上传和下载中文文件，有些地方建议使用以下两句代替
             *  new String(remotePath.getBytes(encoding),"iso-8859-1")转码。
             *  经过测试，通不过。
             */
            // FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
            // conf.setServerLanguageCode("zh");
            // ftpClient.configure(conf);
            // 连接FTP服务器
            ftpClient.connect(url, port);
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftpClient.login(username, password);
            // 设置文件传输类型为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 被动方式
            // ftpClient.enterLocalPassiveMode();
            // ftpClient.setSoTimeout(60 * 1000);
            // ftpClient.setDefaultTimeout(60 * 1000);
            // 获取ftp登录应答代码
            reply = ftpClient.getReplyCode();
            // 验证是否登陆成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                return false;
            }
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
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + File.separator + ff.getName());
                    is = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(fileName, is);
                    IOUtils.closeQuietly(is);
                }
            }
            ftpClient.logout();
            result = true;
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }
        return result;
    }

    public static List<String> readFile(String remotePath, String fileName) {
        List<String> result = new ArrayList<>();
        InputStream in;
        FileWriter is;
        try {
            int reply;
            ftpClient.setControlEncoding(encoding);
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            if (port == 21) {
                ftpClient.connect(url);
            } else {
                ftpClient.connect(url, port);
            }
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftpClient.login(username, password);
            ftpClient.setFileType(FTPClient.FILE_STRUCTURE);
            // 主动方式
            ftpClient.enterLocalActiveMode();
            // 设置文件传输类型为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setDataTimeout(5 * 60 * 1000);
            ftpClient.setConnectTimeout(5 * 60 * 1000);
            ftpClient.setDefaultTimeout(5 * 60 * 1000);
            // 获取ftp登录应答代码
            reply = ftpClient.getReplyCode();
            // 验证是否登陆成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                return result;
            }
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
                    in = ftpClient.retrieveFileStream(ff.getName());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String tempString;
                    while ((tempString = reader.readLine()) != null) {// 一次读入一行，直到读入null为文件结束
                        result.add(tempString);
                    }
                    if (!localFile.exists()) {
                        localFile.createNewFile();
                    }
                    is = new FileWriter(localFile);
                    BufferedWriter bw = new BufferedWriter(is);
                    for (String str : result) {
                        bw.write(str + System.getProperty("line.separator"));
                    }
                    IOUtils.closeQuietly(reader);
                    IOUtils.closeQuietly(bw);
                    ftpClient.completePendingCommand();
                }
            }
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
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
