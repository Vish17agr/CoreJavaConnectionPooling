package com.dbcp2.pooling.db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	private String dbUserName;

	private String dbPassword;

	private String dbUrl;

	private String dbDriver;

	private Integer dbMaxConnections;

	private Integer dbMaxIdle;

	private Integer dbMinIdle;
	
	public String getDB_USER_NAME() {
		return dbUserName;
	}

	public void setDB_USER_NAME(String dB_USER_NAME) {
		dbUserName = dB_USER_NAME;
	}

	public String getDB_PASSWORD() {
		return dbPassword;
	}

	public void setDB_PASSWORD(String dB_PASSWORD) {
		dbPassword = dB_PASSWORD;
	}

	public String getDB_URL() {
		return dbUrl;
	}

	public void setDB_URL(String dB_URL) {
		dbUrl = dB_URL;
	}

	public String getDB_DRIVER() {
		return dbDriver;
	}

	public void setDB_DRIVER(String dB_DRIVER) {
		dbDriver = dB_DRIVER;
	}

	public Integer getDB_MAX_CONNECTIONS() {
		return dbMaxConnections;
	}

	public void setDB_MAX_CONNECTIONS(Integer dB_MAX_CONNECTIONS) {
		dbMaxConnections = dB_MAX_CONNECTIONS;
	}

	public Integer getDbMaxIdle() {
		return dbMaxIdle;
	}

	public void setDbMaxIdle(Integer dbMaxIdle) {
		this.dbMaxIdle = dbMaxIdle;
	}

	public Integer getDbMinIdle() {
		return dbMinIdle;
	}

	public void setDbMinIdle(Integer dbMinIdle) {
		this.dbMinIdle = dbMinIdle;
	}
	
	public Configuration() {
		init();
	}

	private static Configuration configuration = new Configuration();

	public static Configuration getInstance() {
		return configuration;
	}

	private void init(){
		
		Properties prop = new Properties();
		String propFileName = "application.properties";
		
		logger.info("Connection configuration property file used is {}",propFileName);
		
		try {
			FileReader reader = null;
			reader = new FileReader(propFileName);
			if(reader!=null){
				prop.load(reader);
			}
		} catch (FileNotFoundException e1) {
			try {
				prop.load(getClass().getClassLoader().getResourceAsStream(propFileName));
			} catch (IOException e) {
				logger.error("Error occured while loading the configuration property file"+e.getMessage(),e);
			}
		} catch (IOException e1) {
			logger.error("Error occured while loading the configuration property file"+e1.getMessage(),e1);
		}
		
		try {
			setDB_USER_NAME(prop.getProperty("DB_USER"));
			setDB_PASSWORD(prop.getProperty("DB_PWD"));
			setDB_URL(prop.getProperty("DB_URL"));
			setDB_DRIVER("oracle.jdbc.driver.OracleDriver");
			setDB_MAX_CONNECTIONS(getMaxConnectionLimit(prop.getProperty("DB_MAX_CONNECTIONS","5")));
			setDbMaxIdle(getIntegerLimit(prop.getProperty("DB_MAX_IDLE","2")));
			setDbMinIdle(getIntegerLimit(prop.getProperty("DB_MIN_IDLE","1")));
		} catch (Exception e) {
			logger.error("Exception in Configuration init method"+e.getMessage(),e);
		}
		
	}
	
	public int getMaxConnectionLimit(String limit){
		
		try {
			return Integer.parseInt(limit);
		} catch (NumberFormatException e) {
			logger.error("DB_MAX_CONNECTIONS property not passed properly, assigned default value as 5");
			return 5;
		}
		
	}
	
	public int getIntegerLimit(String limit){

		try {
			return Integer.parseInt(limit);
		} catch (NumberFormatException e) {
			logger.error("Integer property not passed properly, assigned default value as 1");
			return 1;
		}
	}
	
}