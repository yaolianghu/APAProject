package org.roadscholar.apa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
import org.roadscholar.apa.util.RSMySQLDatabase;

public class RSDirectoryDAO {
	
	public static Logger logger = Logger.getLogger(RSPageDAO.class);
	
	public static void insertDirectoryList(String RSDirectoryName, String absolutePath, int parentId) {
		logger.debug("addDirectoryList method is called");
		Connection conn = null;
		PreparedStatement pStmt = null;
		String sql = null;
		
		try {
			conn = RSMySQLDatabase.getConnection();
			sql = "insert into RSDirectory values (null,?,?,?)";
			pStmt = RSMySQLDatabase.getPreparedStatment(conn, sql);
			pStmt.setString(1, RSDirectoryName);
			pStmt.setString(2, absolutePath);
			pStmt.setInt(3, parentId);
			pStmt.executeUpdate();
			
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeStatement(pStmt);
			RSMySQLDatabase.closeConn(conn);
		}	
	}
	
	public static boolean existDirectoryId(String absolutePath) {
		logger.debug("exists method is called");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select RSDirectoryId from RSDirectory where absolutePath = '" + absolutePath + "'";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			stmt = RSMySQLDatabase.getStatement(conn);
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			if(rs.next()) {
				return true;
			} 
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeStatement(stmt);
			RSMySQLDatabase.closeConn(conn);
		}	
		return false;
	}
	
	public static int getDirectoryId(String absolutePath) {
		logger.debug("getDirectoryId method is called");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String apr = absolutePath.replace("\\", "\\\\");
		String sql = "select RSDirectoryId from RSDirectory where absolutePath = '" + apr + "'"; 
		int id = 0;
		
		try {
			conn = RSMySQLDatabase.getConnection();
			stmt = RSMySQLDatabase.getStatement(conn);
			rs = RSMySQLDatabase.executeQuery(stmt, sql);
			
			while(rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeStatement(stmt);
			RSMySQLDatabase.closeConn(conn);
		}	
		return id;
	}
}
