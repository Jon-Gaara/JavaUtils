package com.yumaolin.util.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class JDBCUtil {
    /**
     * The server time zone value '�й���׼ʱ��' is unrecognized or represents more than one time zone. 
     * You must configure either the server or JDBC driver (via the serverTimezone configuration property) 
     * to use a more specifc time zone value if you want to utilize time zone support.
     * mysql-connector-java版本过高 建议使用低版本
     */
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String userName="root";
    private static final String passWord="root";
    private static final String url="jdbc:mysql://localhost:3306/sz2yg?useUnicode=true&characterEncoding=UTF-8";
    private static Connection conn = null;
    
    private static Connection getConnection(){
	try{
	    Class.forName(driver);//加载驱动
	    if(conn==null){
		conn = DriverManager.getConnection(url, userName, passWord);//建立连接
	    }
	}catch(Exception e){
	    e.printStackTrace();
	}
	 return conn;
    }
    
    public static List<Object> getUserFind(String userId){
	conn = getConnection();
	StringBuffer sql= new StringBuffer("select * from user ");
	List<Object> list = new ArrayList<Object>(); 
	if(StringUtils.isNotEmpty(userId)){
	    sql.append("where userId =? ");
	    list.add(userId);
	}
	try {
	    PreparedStatement state = conn.prepareStatement(sql.toString());//创建PreparedStatement,预编译sql
	    for(int i=0;i<list.size();i++){
		state.setObject(i, list.get(i));//设置值
	    }
	   ResultSet rs =  state.executeQuery();//执行sql语句
	   int col = rs.getMetaData().getColumnCount();//获取总列数
	   list = new ArrayList<Object>();
	   while(rs.next()){//获取行数
	       StringBuffer str = new StringBuffer();
	      for(int i=1;i<=col;i++){//获取列值
		  str.append(rs.getString(i)+" ");
		  
	      }
	      list.add(str);
	      System.out.print(str);
	      System.out.println();
	   }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return list;
    }
    
    
}
