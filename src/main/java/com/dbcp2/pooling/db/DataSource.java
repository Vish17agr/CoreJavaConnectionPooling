package com.dbcp2.pooling.db;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
	
	static Dbcp2ConnectionPool dbcp2Pool = new Dbcp2ConnectionPool();
	
	public static Connection getDbcpConnection() throws SQLException {
		return dbcp2Pool.getConnectionFromPool();
	}
	
	public static void getDbcpPoolStatus() {
		dbcp2Pool.printDbStatus();
	}
	
	public static void checkDbcpPool() {
		dbcp2Pool.checkPoolConnection();
	}
}
