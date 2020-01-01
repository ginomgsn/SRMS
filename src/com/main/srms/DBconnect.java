package com.main.srms;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect {
	public static final String USERNAME = "root";
	public static final String PASSWORD = "acds";
	public static final String CONN = "jdbc:mysql://localhost/srms";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(CONN, USERNAME, PASSWORD);
	}
}
