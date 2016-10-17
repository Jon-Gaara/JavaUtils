package com.yumaolin.util.Test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.yumaolin.util.JDBC.JDBCUtil;

public class JdbcJunit {
    @Test
    public void jdbcTest(){
	List<List<String>> users = new ArrayList<List<String>>();
	for(int i=0;i<100000;i++){
	    List<String> user = new ArrayList<String>();
	    user.add(i+"");
	    user.add("jack"+i);
	    user.add("35");
	    user.add("0");
	    users.add(user);
	}
	long start = System.currentTimeMillis();
	JDBCUtil.addOrUpdateBatch(users);
	System.out.println(System.currentTimeMillis()-start);
    }
}
