package org.cdm.template;

import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import online.sanen.cdm.template.JdbcTemplate;

public class CommenTest {

	public static void main(String[] args) throws Exception {

		DruidDataSource druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(new Properties() {
			private static final long serialVersionUID = 1L;
			{
				setProperty("driverClassName", "com.mysql.jdbc.Driver");
				setProperty("url", "jdbc:mysql://127.0.0.1:3306/meet?useSSL=false");
				setProperty("username", "root");
				setProperty("password", "root");
				setProperty("validationQuery", "select 1");
			}
		});

		druidDataSource.setTestOnBorrow(false);
		druidDataSource.setRemoveAbandoned(false);
		druidDataSource.setMaxWait(5000);
		

		JdbcTemplate template = new JdbcTemplate(druidDataSource);
		
		Dept dept = template.queryForEntry(Dept.class,"select * from dept where id=?", 1);
		
		System.out.println(dept);
	}

}
