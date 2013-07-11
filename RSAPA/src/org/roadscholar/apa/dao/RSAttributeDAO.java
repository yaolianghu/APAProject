package org.roadscholar.apa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.roadscholar.apa.RSAttribute;
import org.roadscholar.apa.util.RSMySQLDatabase;

public class RSAttributeDAO {
	
	public static Logger logger = Logger.getLogger(RSPageDAO.class);
	
	public static void addAttribute(RSAttribute rsa) {
		PropertyConfigurator.configure("log4j.properties");
		logger.debug("addAttribute method is called");
		Connection conn = null;
		PreparedStatement pStmt = null;
		String sql = "insert into RSAttribute values(null, ?, ?, ?, ?)";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			pStmt = RSMySQLDatabase.getPreparedStatment(conn, sql);
			pStmt.setString(1, rsa.getRSAttributeName());
			pStmt.setString(2, rsa.getRSAttributeDescription());
			pStmt.setTimestamp(3, new Timestamp(rsa.getRSAttributeCreateDate().getTime()));
			pStmt.setTimestamp(4, new Timestamp(rsa.getRSAttributeModifiedDate().getTime()));
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeStatement(pStmt);
			RSMySQLDatabase.closeConn(conn);
		}
	}
	
	public static int totalNumberOfAttribute() {
		PropertyConfigurator.configure("log4j.properties");
		logger.debug("totalNumberOfAttribute method is called");
		Connection conn = null;
		ResultSet rs = null;
		int totalNumberOfAttribute = 0;
		
		String sql = "select count(*) from RSAttribute";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			if(rs.next()) {
				totalNumberOfAttribute = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}	
		return totalNumberOfAttribute;
	}

	public static String searchAttributeNameByAttributeId (int RSAttributeId) {
		PropertyConfigurator.configure("log4j.properties");
		logger.debug("searchAttributeNameByAttributeId method is called");
		Connection conn = null;
		ResultSet rs = null;
		String RSAttributeName = null;
		
		String sql = "select url from RSAttributeName where RSAttributeId = " + RSAttributeId;
		
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			if(rs.next()) {
				RSAttributeName = rs.getString("RSAttributeName");
			} 
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}	
		return RSAttributeName;
	}
	
}
