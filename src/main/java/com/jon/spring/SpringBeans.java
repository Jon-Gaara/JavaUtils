package com.jon.spring;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class SpringBeans extends ApplicationObjectSupport {
    private final static String springFilePath = "classpath*:spring/applicationContext.xml";
    // private final static ApplicationContext context =new FileSystemXmlApplicationContext(new
    // String[]{springFilePath});
    private final static ApplicationContext context = new ClassPathXmlApplicationContext(springFilePath);
    // private final static GenericXmlApplicationContext applicationContext =getGenericXmlApplicationContext();
    // private final static DefaultListableBeanFactory dbr = getDefaultListableBeanFactory();
    // private final static BeanFactory beanFactory = new ClassPathXmlApplicationContext(springFilePath);

    public static Connection getConnection() {
        ComboPooledDataSource dataSource = context.getBean("dataSource", ComboPooledDataSource.class);
        // ComboPooledDataSource dataSource = (ComboPooledDataSource) applicationContext.getBean("dataSource");
        // ComboPooledDataSource dataSource = (ComboPooledDataSource)dbr.getBean("dataSource");
        // ComboPooledDataSource dataSource = (ComboPooledDataSource)beanFactory.getBean("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*private static GenericXmlApplicationContext getGenericXmlApplicationContext(){
    GenericXmlApplicationContext context = new GenericXmlApplicationContext();
    context.setValidating(false);
    context.load(springFilePath);
    context.refresh();
    return context;
    }*/

    /*
     //此方法不支持${jdbc.driverClassName}类似的
    private static DefaultListableBeanFactory getDefaultListableBeanFactory(){
    DefaultListableBeanFactory context = new DefaultListableBeanFactory();
    XmlBeanDefinitionReader xmlBean = new XmlBeanDefinitionReader(context);
    xmlBean.loadBeanDefinitions(springFilePath);
    return context;
    }*/

    public static void main(String[] args) throws SQLException {
        DatabaseMetaData dbmd = SpringBeans.getConnection().getMetaData();
		// 判断该数据库支不支持批量更新
        boolean a = dbmd.supportsBatchUpdates();
        System.out.println(a);
    }
}
