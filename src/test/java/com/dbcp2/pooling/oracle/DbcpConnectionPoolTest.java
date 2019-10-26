package com.dbcp2.pooling.oracle;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

import com.dbcp2.pooling.db.DataSource;

public class DbcpConnectionPoolTest {

	Connection connection;
	
	@Before
    public void before() {
        try {
        	connection = DataSource.getDbcpConnection();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	@Test
	public void test() {
		assertNotNull(connection);
	}

}