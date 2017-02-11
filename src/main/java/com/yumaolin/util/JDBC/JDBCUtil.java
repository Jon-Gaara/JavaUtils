package com.yumaolin.util.JDBC;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


@Service
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
		/**
		 * 启用JDBC跟踪信息
		 */
		PrintWriter pw = new PrintWriter(new File("d:/jdbc.out"));
		DriverManager.setLogWriter(pw);
		DatabaseMetaData metadata  =conn.getMetaData();
		 System.out.println("数据库已知的用户: "+ metadata.getUserName());   
	         System.out.println("数据库的系统函数的逗号分隔列表: "+ metadata.getSystemFunctions());   
	         System.out.println("数据库的时间和日期函数的逗号分隔列表: "+ metadata.getTimeDateFunctions());   
	         System.out.println("数据库的字符串函数的逗号分隔列表: "+ metadata.getStringFunctions());   
	         System.out.println("数据库供应商用于 'schema' 的首选术语: "+ metadata.getSchemaTerm());   
	         System.out.println("数据库URL: " + metadata.getURL());   
	         System.out.println("是否允许只读:" + metadata.isReadOnly());   
	         System.out.println("数据库的产品名称:" + metadata.getDatabaseProductName());   
	         System.out.println("数据库的版本:" + metadata.getDatabaseProductVersion());   
	         System.out.println("驱动程序的名称:" + metadata.getDriverName());   
	         System.out.println("驱动程序的版本:" + metadata.getDriverVersion());  
	         
 	    }
	}catch(Exception e){
	    e.printStackTrace();
	}
	 return conn;
    }
    
    /**
     * 查询
     */
    public static String[][] getUserFind(String userId){
	conn = getConnection();
	StringBuffer sql= new StringBuffer("select * from user ");
	List<Object> list = new ArrayList<Object>(); 
	String[][] value = null;
	if(StringUtils.isNotEmpty(userId)){
	    sql.append("where id =? ");
	    list.add(userId);
	}
	try {
	    PreparedStatement state = conn.prepareStatement(sql.toString());//创建PreparedStatement,预编译sql
	    for(int i=0;i<list.size();i++){
		state.setObject(i+1, list.get(i));//设置值
	    }
	   ResultSet rs =  state.executeQuery();//执行sql语句
	   //conn.getMetaData().supportsResultSetType(rs.getType());//判断数据库是否支持可滚动
	   //conn.getMetaData().supportsResultSetConcurrency(rs.getType(),rs.getConcurrency());//判断数据库是否支持可更新
	   int col = rs.getMetaData().getColumnCount();//获取总列数
	   rs.last();//游标滚动到最后一行
	   int row = rs.getRow();//获取总行数
	   rs.beforeFirst();//游标滚动到第一行之前
	   value = new String[row][col];
	   int j=0;
	   while(rs.next()){//获取行数
	      for(int i=1;i<=col;i++){//获取列值
		  value[j][i-1]=rs.getString(i);
	      }
	      j++;
	   }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return value;
    }
    
    /**
     * 批量更新或插入
     */
    public static void addOrUpdateBatch(List<List<Object>> users){
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
    		  更新ResultSet只能更新一张表或者是以主键为条件连接的多表查询结果集
    		*/
		PreparedStatement prepStateMent  = conn.prepareStatement(str,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		for(int i=0;i<users.size();i++){
		    prepStateMent.setObject(1,users.get(i).get(0));
		    prepStateMent.setObject(2,users.get(i).get(1));
		    prepStateMent.setObject(3,users.get(i).get(2));
		    prepStateMent.setObject(4,users.get(i).get(3));
		    prepStateMent.addBatch();
		   if((i+1)%1000==0){
		       prepStateMent.executeBatch();
		       //prepStateMent.executeLargeUpdate(sql, autoGeneratedKeys)
		       //prepStateMent.execute(str,Statement.RETURN_GENERATED_KEYS);//自动生成第一列的键
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
