package com.yumaolin.util.Test;

import org.junit.Test;

import com.yumaolin.util.JDBC.JDBCUtil;

public class JdbcJunit {
    @Test
    public void jdbcTest(){
	JDBCUtil.getUserFind(null);
    }
    
}
