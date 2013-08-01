package org.roadscholar.apa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
import org.roadscholar.apa.RSPageAttributeAffiliation;
import org.roadscholar.apa.util.RSMySQLDatabase;

public class RSPageAttributeAffiliationDAO {
	
	public static Logger logger = Logger.getLogger(RSPageDAO.class);
	
	public static void insertPageAttributeAffiliationList(RSPageAttributeAffiliation rspaa) {
		logger.debug("addPageAttributeAffiliation method is called");
		Connection conn = null;
		PreparedStatement pStmt = null;
		String sql = "insert into RSPageAttributeAffiliation values(null, ?, ?, ?, ?)";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			pStmt = RSMySQLDatabase.getPreparedStatment(conn, sql);
			pStmt.setInt(1, rspaa.getRSPageId());
			pStmt.setInt(2, rspaa.getRSAttributeId());
			pStmt.setString(3, rspaa.getRSPageAttributeAffiliationDescription());
			pStmt.setInt(4, rspaa.getRSPageAttributeAffiliationSector());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeStatement(pStmt);
			RSMySQLDatabase.closeConn(conn);
		}
	}
	
	public static ArrayList<String> checkPageAttributeAffiliationDescription(int RSPageId, int RSAttributeId) {
		logger.debug("checkPageAttributeAffiliationDescription method is called");
		Connection conn = null;
		ResultSet rs = null;
		ArrayList<String> pageAttributeAffiliationDescription = null;
		
		String sql = "select RSPageAttributeAffiliationDescription from " +
				"RSPageAttributeAffiliation where rspageId = " + RSPageId + " and rsattributeId =" + RSAttributeId;
		
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			pageAttributeAffiliationDescription = new ArrayList<String>();
			
			while(rs.next()) {
				pageAttributeAffiliationDescription.add(rs.getString("RSPageAttributeAffiliationDescription"));
			}
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}
		
		return pageAttributeAffiliationDescription;
	}

	public static void deleteRSPageAttributeAffiliation(int RSPageId,
			int RSAttributeId, String RSPageAttributeAffiliationDescription) {
		logger.debug("deleteRSPageAttributeAffiliation method is called");
		Connection conn = null;
		Statement stmt = null;
		
		String sql = "delete from RSPageAttributeAffiliation where rspageId = " + RSPageId + 
				" and rsattributeId =" + RSAttributeId + " and RSPageAttributeAffiliationDescription = '" 
				+ RSPageAttributeAffiliationDescription + "'";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			stmt = RSMySQLDatabase.getStatement(conn);
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeStatement(stmt);
			RSMySQLDatabase.closeConn(conn);
		}
		
	}
}
