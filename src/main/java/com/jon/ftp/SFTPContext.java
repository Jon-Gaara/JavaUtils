package com.jon.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPContext {
    private static String host = "127.0.0.1";
    private static int port = 7393;
    private static String username = "fjrssc";
    private static String password = "123123123";
    private static Session sshSession;

    /**
     * 连接sftp服务器
     */
    public static ChannelSftp connect() {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
            sshConfig.put("StrictHostKeyChecking", "no");
            // 设置登陆超时时间
            sshSession.setConfig(sshConfig);
			// 设置timeout时间
            sshSession.setTimeout(30000);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp)channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sftp;
    }

    /**
     * 上传文件
     * 
     * @param directory 上传的目录
     * @param uploadFile 要上传的文件
     */
    public static void upload(String directory, InputStream uploadFile, String fileName, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.put(uploadFile, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * 
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     * @throws IOException
     */
    public static void download(String downloadFile, String saveFile, ChannelSftp sftp)
        throws IOException {
        FileOutputStream fileOut = null;
        try {
            // sftp.cd(directory);
            File file = new File(saveFile);
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOut = new FileOutputStream(saveFile);
            sftp.get(downloadFile, fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                fileOut.close();
            }
        }
    }

    /**
     * 列出目录下的文件
     * 
     * @param directory 要列出的目录
     * @param sftp
     */
    public static Vector<?> listFiles(String directory, ChannelSftp sftp, String fileType) throws SftpException {
        sftp.cd(directory);
        Vector<?> list = sftp.ls(fileType);
        return list;
    }

    public static void main(String[] args) throws Exception {
        ChannelSftp sftp = SFTPContext.connect();
        Vector<?> list = SFTPContext.listFiles("/", sftp, "*.TXT");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        sftp.getSession().disconnect();
        // SFTPContext.download("/", "COD2100024440_003_20150910_01.TXT", "d:/COD2100024440_003_20150910_01.TXT", sftp);
        // new SFTPDownLoadFile().execute();
        /*String str  = String.format("%-64s","测试商户");
        byte[] bytes = str.getBytes("utf-8");
        byte[] newByte = new byte[64];
        System.arraycopy(bytes, 0, newByte, 0, newByte.length);
        System.out.println(new String(newByte).length());*/
    }
}