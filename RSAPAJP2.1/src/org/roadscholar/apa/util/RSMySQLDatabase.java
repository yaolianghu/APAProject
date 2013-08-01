package org.roadscholar.apa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class RSMySQLDatabase {
	public static Logger logger = Logger.getLogger(RSMySQLDatabase.class);
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");	
		} catch (ClassNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
			System.exit(0);
		}	
	}
	
	public static Connection getConnection() {
		logger.debug("getConnection method is called");
		Connection conn = null;
		String url = "jdbc:mysql://172.16.1.38:3306/RSDatabase?user=hu&password=rstransit12!";
		
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
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
				logger.error(e);
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

}
