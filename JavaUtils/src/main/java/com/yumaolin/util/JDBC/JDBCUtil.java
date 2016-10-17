package com.yumaolin.util.JDBC;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
    private static final String url="jdbc:mysql://localhost:3306/testdatebase?useUnicode=true&characterEncoding=UTF-8";
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
    
    /**
     * 查询
     */
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
	   }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return list;
    }
    
    /**
     * 批量更新或插入
     */
    public static void addOrUpdateBatch(List<List<String>> users){
	String str = "insert into user(id,name,age,sex) values(?,?,?,?)";
	try{
	    conn = getConnection();
	    DatabaseMetaData dbmd= conn.getMetaData();
	    boolean a=dbmd.supportsBatchUpdates();//判断该数据库支不支持批量更新
	    if(a){
		conn.setAutoCommit(false);
		/*createStatement返回的Statement对象是执行sql语句用的
    		    第一个参数可以取值为
    		  ResultSet.RTYPE_FORWORD_ONLY,缺省类型。只允许向前访问一次，并且不会受到其他用户对该数据库所作更改的影响。
    		  ResultSet.TYPE_SCROLL_INSENSITIVE,双向滚动，但不及时更新，就是如果数据库里的数据修改过，并不在ResultSet中反应出来。
    		  ResultSet.TYPE_SCROLL_SENSITIVE，双向滚动，并及时跟踪数据库的更新,以便更改ResultSet中的数据。
    		    第二个参数可以取值为
    		  ResultSet.CONCUR_READ_ONLY：这是缺省值，指定不可以更新 ResultSet
    		  ResultSet.CONCUR_UPDATABLE：指定可以更新 ResultSet
    		*/
		PreparedStatement prepStateMent  = conn.prepareStatement(str,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		for(int i=0;i<users.size();i++){
		    prepStateMent.setObject(1,users.get(i).get(0));
		    prepStateMent.setObject(2,users.get(i).get(1));
		    prepStateMent.setObject(3,users.get(i).get(2));
		    prepStateMent.setObject(4,users.get(i).get(3));
		    prepStateMent.addBatch();
		   if((i+1)%1000==0){
		       prepStateMent.executeBatch();
		       conn.commit();//33.312秒 不加37.871秒
		   }
		}
		 prepStateMent.executeBatch();
		 conn.commit();
		 conn.close();
	    }
	}catch(Exception e){
	    e.printStackTrace();
	    try {
		conn.rollback();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}
    }
}
