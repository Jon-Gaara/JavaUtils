package com.yumaolin.util.Test;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.Test;

public class SocketTest {

    @Test
    public void Socket() throws Exception{
	try(Socket socket = new Socket()){
	    socket.setSoTimeout(10000);
	    socket.connect(new InetSocketAddress("java.sun.com", 80),10000);
	    InetAddress[] inet = InetAddress.getAllByName("www.baidu.com");
	    System.out.println(Arrays.toString(inet));
	    System.out.println(InetAddress.getLocalHost());
	    InputStream input = socket.getInputStream();
	    Scanner sc = new Scanner(input);
	    while(sc.hasNextLine()){
		String line  = sc.nextLine();
		System.out.println(line);
	    }
	}
    }
}
