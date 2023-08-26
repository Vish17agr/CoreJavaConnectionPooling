# CoreJavaConnectionPooling
Implementation of connection pooling in core java without JNDI
This source code explain about the connection pooling with oracle and postgres database.

# Download ojdbc7 using below command if connecting to oracle database
mvn install:install-file  -Dfile=C:\\app\\oracle\\product\\12.1.0\\client\\jdbc\\lib\\ojdbc7.jar  -DgroupId=com.oracle  -DartifactId=ojdbc7  -Dversion=12.1.0   -Dpackaging=jar	

# Properties
Update application.properties file to provide database details

# Run
To run the application excuete below command
mvn compile exec:java -Dexec.mainClass="com.dbcp2.pooling.main.Execution"
