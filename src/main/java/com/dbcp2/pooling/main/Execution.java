package com.dbcp2.pooling.main;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbcp2.pooling.db.DataSource;

public class Execution {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	public static void main(String[] args) throws InterruptedException {
		
		Connection dbConnection = null;
		
		try {
			dbConnection = DataSource.getDbcpConnection();
			DataSource.getDbcpPoolStatus();
		} catch (Exception e){
			logger.error(e.getMessage(),e);
		} finally{
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


