package com.dbcp2.pooling.db;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dbcp2ConnectionPool {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

	private GenericObjectPool<PoolableConnection> gPool = null;
	
	private DataSource dataSource;
	
	public Dbcp2ConnectionPool() {
		try {
			dataSource = setUpPool();
			checkPoolConnection();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	public DataSource setUpPool() throws ClassNotFoundException {
		
		Configuration dbConfig = Configuration.getInstance();
		Class.forName(dbConfig.getDB_DRIVER());

		GenericObjectPoolConfig<PoolableConnection> config = new GenericObjectPoolConfig<>();
		config.setMaxTotal(dbConfig.getDB_MAX_CONNECTIONS());
		config.setMaxIdle(dbConfig.getDbMaxIdle());
		config.setMinIdle(dbConfig.getDbMinIdle());

		Properties connectionProps = new Properties();
		connectionProps.put("user", dbConfig.getDB_USER_NAME());
		connectionProps.put("password", dbConfig.getDB_PASSWORD());
		connectionProps.put("create", "true");

		/* 
		 * Creates a ConnectionFactory Object Which Will Be Use by the Pool to create the Connection Object!
		 */
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				dbConfig.getDB_URL(), connectionProps);

		ObjectName poolName = null;
		try {
			poolName = new ObjectName("com.dbcp2", "connectionPool","oraclePool");
		} catch (MalformedObjectNameException e) {
			logger.error(e.getMessage(),e);
		}

		/* 
		 * Creates a PoolableConnectionFactory That Will Wraps the Connection
		 * object Created by the ConnectionFactory to Add Object Pooling Functionality!
		 */
		PoolableConnectionFactory pooledObjectFactory = new PoolableConnectionFactory(
				connectionFactory, poolName);

		/* 
		 * Creates an Instance of GenericObjectPool That Holds Our Pool of connections Object!
		 */
		gPool = new GenericObjectPool<>(pooledObjectFactory,config);
		pooledObjectFactory.setPool(gPool);

		return new PoolingDataSource<PoolableConnection>(gPool);
	}

	public GenericObjectPool<PoolableConnection> getConnectionPool() {
		return gPool;
	}

	/*
	 * This Method Is Used To Print The Connection Pool Status
	 */
	public synchronized void printDbStatus() {
		logger.info("Connection details:	Max.: {} ; Active: {} ; Idle: {} ", getConnectionPool().getMaxTotal(),
				getConnectionPool().getNumActive(), getConnectionPool().getNumIdle());
	}
	
	public synchronized Connection getConnectionFromPool() throws SQLException  {
		return dataSource.getConnection();
	}

	public synchronized void checkPoolConnection(){

		printDbStatus();
		
		// Performing Database Operation!
		logger.info("=====Making A New Connection Object For Db Transaction=====");
		
		try(Connection connObj = dataSource.getConnection()) {

			printDbStatus();
			
			String query = "select sysdate from dual";

			try(PreparedStatement pstmtObj = connObj.prepareStatement(query)){

				try(ResultSet rsObj = pstmtObj.executeQuery()){
					if (rsObj != null && rsObj.next()) {
						logger.info("DB Sysdate: {}",rsObj.getDate("sysdate"));
					}
				}
			}

			logger.info("=====Releasing Connection Object To Pool=====");

		} catch (SQLException sqlException) {
			logger.error(sqlException.getMessage(),sqlException);
		} 
		printDbStatus();
	}
	
}

