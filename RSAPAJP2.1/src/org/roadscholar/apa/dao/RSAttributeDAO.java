package org.roadscholar.apa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
import org.roadscholar.apa.RSAttribute;
import org.roadscholar.apa.util.RSMySQLDatabase;

public class RSAttributeDAO {
	
	public static Logger logger = Logger.getLogger(RSPageDAO.class);
	
	public static void addAttribute(RSAttribute rsa) {
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
	
	public static ArrayList<Integer> attributeIdList() {
		logger.debug("totalNumberOfAttribute method is called");
		Connection conn = null;
		ResultSet rs = null;
		ArrayList<Integer> attributeIdList = new ArrayList<Integer>();
		
		String sql = "select RSAttributeId from RSAttribute order by RSAttributeId";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			while(rs.next()) {
				attributeIdList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}	
		return attributeIdList;
	}

	public static String searchAttributeNameByAttributeId (int RSAttributeId) {
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
