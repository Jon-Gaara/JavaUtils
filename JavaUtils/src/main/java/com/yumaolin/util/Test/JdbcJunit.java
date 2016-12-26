package com.yumaolin.util.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.yumaolin.util.JDBC.JDBCUtil;

public class JdbcJunit {
    @Test
    public void jdbcTest(){
	List<List<Object>> users = new ArrayList<List<Object>>();
	for(int i=0;i<100000;i++){
	    List<Object> user = new ArrayList<Object>();
	    user.add("0");
	    user.add(new Timestamp(System.currentTimeMillis()));
	    users.add(user);
	}
	long start = System.currentTimeMillis();
	JDBCUtil.addOrUpdateBatch(users);
	System.out.println(System.currentTimeMillis()-start);
    }
}
