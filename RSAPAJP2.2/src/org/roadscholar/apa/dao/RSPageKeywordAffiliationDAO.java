package org.roadscholar.apa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.roadscholar.apa.RSPageKeywordAffiliation;
import org.roadscholar.apa.util.RSMySQLDatabase;

public class RSPageKeywordAffiliationDAO {
	
	public static Logger logger = Logger.getLogger(RSPageDAO.class);
	
	public static void insertPageKeywordAffiliation(RSPageKeywordAffiliation rspka) {
		logger.debug("insertPageKeywordAffiliation method is called");
		Connection conn = null;
		PreparedStatement pStmt = null;
		String sql = "insert into RSPageKeywordAffiliation values(null, ?, ?, ?, ?)";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			pStmt = RSMySQLDatabase.getPreparedStatment(conn, sql);
			pStmt.setInt(1, rspka.getRSPageId());
			pStmt.setInt(2, rspka.getRSKeywordId());
			pStmt.setString(3, rspka.getRSPageKeywordAffiliationDescription());
			pStmt.setInt(4, rspka.getRSPageKeywordAffiliationSector());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeStatement(pStmt);
			RSMySQLDatabase.closeConn(conn);
		}
	}
	
	public static boolean existPageKeywordAffiliationDescription(int RSPageId, int RSKeywordId,
						int RSPageKeywordAffiliationSector) {
		logger.debug("checkPageAttributeAffiliationDescription method is called");
		Connection conn = null;
		ResultSet rs = null;
		
		String sql = "select * from RSPageKeywordAffiliation" +
				" where RSPageId = " + RSPageId + " and RSKeywordId = " + RSKeywordId + 
				" and RSPageKeywordAffiliationSector = " + RSPageKeywordAffiliationSector;
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}
		
		return false;
	}
}
