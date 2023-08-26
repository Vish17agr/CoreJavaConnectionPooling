package com.dbcp2.pooling.oracle;

import static org.junit.Assert.*;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

import com.dbcp2.pooling.db.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DbcpConnectionPoolTest {

	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getName());

	Connection connection;
	
	@Before
    public void before() {
        try {
        	connection = DataSource.getDbcpConnection();
        } catch (Exception e) {
			logger.error(e.getMessage(),e);
        }
    }

	@Test
	public void test() {
		assertNotNull(connection);
	}

}