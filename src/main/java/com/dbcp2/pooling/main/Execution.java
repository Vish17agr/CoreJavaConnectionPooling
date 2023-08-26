package com.dbcp2.pooling.main;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dbcp2.pooling.db.DataSource;

public class Execution {
	
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	public static void main(String[] args) {
		
		Connection dbConnection = null;
		
		try {
			dbConnection = DataSource.getDbcpConnection();
			DataSource.getDbcpPoolStatus();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
			try {
				if(dbConnection != null){
					dbConnection.close();
				}
				DataSource.getDbcpPoolStatus();
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
			}
		}
	
	}//main
	
}//Class Execution


