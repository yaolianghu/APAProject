package org.roadscholar.apa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RSMySQLDatabase {
	public static Logger logger = Logger.getLogger(RSMySQLDatabase.class);
	
	static {
		PropertyConfigurator.configure("log4j.properties");
		try {
			Class.forName("com.mysql.jdbc.Driver");	
		} catch (ClassNotFoundException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
			System.exit(0);
		}	
	}
	
	public static Connection getConnection() {
		logger.debug("getConnection method is called");
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/RSDatabase?user=root&password=tiger";
		
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
			System.exit(0);
		}
		
		return conn;
	}
	
	public static Statement getStatement(Connection conn) {
		logger.debug("getStatement method is called");
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
			System.exit(0);
		}
		
		return stmt;
	}
	
	public static PreparedStatement getPreparedStatment(Connection conn, String sql) {
		logger.debug("getPreparedStatment method is called");
		PreparedStatement pStmt = null;
		
		try {
			pStmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
			System.exit(0);
		}
		return pStmt;
	}
	
	public static ResultSet executeQuery(Statement stmt, String sql) {
		logger.debug("executeQuery method is called");
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
			System.exit(0);
		}
		return rs;
	}
	
	public static ResultSet executeQuery(Connection conn, String sql) {
		logger.debug("executeQuery method is called");
		ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
			System.exit(0);
		}
		return rs;
	}
	
	public static void closeConn(Connection conn) {
		logger.debug("closeConn method is called");
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				//PropertyConfigurator.configure("log4j.properties");
				logger.error(e);
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		logger.debug("closeResultSet method is called");
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				//PropertyConfigurator.configure("log4j.properties");
				logger.error(e);
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	public static void closeStatement(Statement stmt) {
		logger.debug("closeStatement method is called");
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				//PropertyConfigurator.configure("log4j.properties");
				logger.error(e);
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

}
