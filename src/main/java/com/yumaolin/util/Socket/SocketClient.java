package com.yumaolin.util.Socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class SocketClient {
	private static Socket socket;
	class MyClientReader implements Runnable{
		private DataInputStream dataInput;
		
		public MyClientReader(DataInputStream dataInput){
			this.dataInput = dataInput;
		}
		
		public void run(){
			String info;
			try {
				while(true){
					info = dataInput.readUTF();
					System.out.println("服务器 :"+info);
					if("bye".equals(info)){
						System.out.println("对方下线，程序退出!");
						System.exit(0);
					}
				}
			} catch (IOException e) {
				if(e.toString().contains("java.net.SocketException: Connection reset")){
					System.out.println("服务器连接中断!");
					System.exit(0);
				}
			}
		}
	}
	
	class MyClientWrite implements Runnable{
		private DataOutputStream data;
		public MyClientWrite(DataOutputStream data){
			this.data = data;
		}
		
		public void run(){
			try {
			    //读取键盘输入流
		    	    InputStreamReader isr = new InputStreamReader(System.in);
			    //封装键盘输入流
			    BufferedReader br = new BufferedReader(isr);
			    while(true){
				String thisInfo = br.readLine();
				if("bye".equals(thisInfo) || thisInfo==null){
					System.out.println("对方下线，程序退出!");
					System.exit(0);
				}
				System.out.println(thisInfo);
				data.writeUTF(thisInfo);
			   }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	 public static void main(String[] args) throws IOException{
	     	socket = new Socket("localhost",8190);
		try{
			// 创建socket对象，指定服务器的ip地址，和服务器监听的端口号
			// 客户端在new的时候，就发出了连接请求，服务器端就会进行处理，如果服务器端没有开启服务，那么
			// 这时候就会找不到服务器，并同时抛出异常==》java.net.ConnectException: Connection
        		InputStream input = socket.getInputStream();//获取输入流
        		//socket.setSoTimeout(10*1000);//设置接收数据超时时间
        		DataInputStream dataInput = new DataInputStream(input);
        		OutputStream output = socket.getOutputStream();//获取输入流
        		DataOutputStream data = new DataOutputStream(output);
        		
			new Thread(new SocketClient().new MyClientReader(dataInput)).start();
			new Thread(new SocketClient().new MyClientWrite(data)).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
