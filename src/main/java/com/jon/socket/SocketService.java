package com.jon.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * https://www.ibm.com/developerworks/java/library/j-javaio/
 */
public class SocketService {
    class MyServerReader implements Runnable {
        private DataInputStream dataInput;

        public MyServerReader(DataInputStream dataInput) {
            this.dataInput = dataInput;
        }

        @Override
		public void run() {
            String info;
            try {
                while (true) {
                    info = dataInput.readUTF();
                    System.out.println("客户端 :" + info);
                    if ("bye".equals(info)) {
                        System.out.println("对方下线");
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("客户端连接结束: " + e.getMessage());
            } finally {
                // 结束本连接的读线程时关闭底层 socket，写线程会随之退出
                try {
                    dataInput.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    class MyServerWrite implements Runnable {
        private DataOutputStream data;

        public MyServerWrite(DataOutputStream data) {
            this.data = data;
        }

        @Override
		public void run() {
            try {
                // 读取键盘输入流
                InputStreamReader isr = new InputStreamReader(System.in);
                // 封装键盘输入流
                BufferedReader br = new BufferedReader(isr);
                while (true) {
                    String thisInfo = br.readLine();
                    if (thisInfo == null || "bye".equals(thisInfo)) {
                        System.out.println("对方下线");
                        break;
                    }
                    data.writeUTF(thisInfo);
                }
            } catch (IOException e) {
                System.out.println("本端写线程结束: " + e.getMessage());
            } finally {
                try {
                    data.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    class MyServiceSocket implements Runnable {
        private Socket socket;

        public MyServiceSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
		public void run() {
            try {
                OutputStream output = socket.getOutputStream();
                InputStream is = socket.getInputStream();
                DataOutputStream data = new DataOutputStream(output);
                DataInputStream dis = new DataInputStream(is);

                Thread t1 = new Thread(new SocketService().new MyServerReader(dis));
                t1.setDaemon(true);
                Thread t2 = new Thread(new SocketService().new MyServerWrite(data));
                t2.setDaemon(true);

                t1.start();
                t2.start();
                // socket 由读/写线程结束时各自关闭，不能在 start() 后立刻关闭
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        try (ServerSocket service = new ServerSocket(8190)) {
            int i = 1;
            while (true) {
				// 获取连接
                Socket socket = service.accept();
                socket.setKeepAlive(true);
                System.out.println("Spwaing :" + i);
                if (socket.isConnected()) {
                    DataOutputStream data = new DataOutputStream(socket.getOutputStream());
                    data.writeUTF("Hello!Enter bye is EXIT");
                }
                new Thread(new SocketService().new MyServiceSocket(socket)).start();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
