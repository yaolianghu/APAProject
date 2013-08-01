package org.roadscholar.apa.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.roadscholar.apa.util.RSMySQLDatabase;

public class RSKeywordDAO {
	public static Logger logger = Logger.getLogger(RSPageDAO.class);
	
	public static ArrayList<Integer> keywordIdList() {
		logger.debug("totalNumberOfAttribute method is called");
		Connection conn = null;
		ResultSet rs = null;
		ArrayList<Integer> keywordIdList = new ArrayList<Integer>();
		
		String sql = "select RSKeywordId from RSKeyword order by RSKeywordId";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			while(rs.next()) {
				keywordIdList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}	
		return keywordIdList;
	}

	public static String searchKeywordNameByKeywordId(int RSKeywordId) {
		logger.debug("searchURLByPageId method is called");
		Connection conn = null;
		ResultSet rs = null;
		String keyword = null;
		
		String sql = "select RSKeywordName from RSKeyword where RSKeywordId = " + RSKeywordId;
		
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			if(rs.next()) {
				keyword = rs.getString("RSKeywordName");
			} 
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}	
		return keyword;
	}
}
