package org.roadscholar.apa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
import org.roadscholar.apa.RSPage;
import org.roadscholar.apa.util.RSMySQLDatabase;

public class RSPageDAO {

	public static Logger logger = Logger.getLogger(RSPageDAO.class);
	
	public static void insertPageList(RSPage rsp) {
		//PropertyConfigurator.configure("log4j.properties");
		logger.debug("addPageFile method is called");
		Connection conn = null;
		PreparedStatement pStmt = null;
		String sql = null;
		
		try {
			conn = RSMySQLDatabase.getConnection();
			sql = "insert into RSPage values (null,?,?,?,?,?,?,?,?,?,?,?)";
			pStmt = RSMySQLDatabase.getPreparedStatment(conn, sql);
			pStmt.setString(1, rsp.getRSPageName());
			pStmt.setString(2, rsp.getNote());
			pStmt.setString(3, rsp.getUrl());
			pStmt.setInt(4, rsp.getRSDirectoryId());
			pStmt.setInt(5, rsp.getCharacterCount());
			pStmt.setInt(6, rsp.getWordCount());
			pStmt.setInt(7, rsp.getTagCount());
			pStmt.setInt(8, rsp.getLineCount());
			pStmt.setTimestamp(9, new Timestamp(rsp.getRSPageCreateDate().getTime()));
			pStmt.setTimestamp(10, new Timestamp(rsp.getRSPageModifiedDate().getTime()));
			pStmt.setTimestamp(11, new Timestamp(rsp.getRSPageLastAccessDate().getTime()));
			pStmt.executeUpdate();			
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeStatement(pStmt);
			RSMySQLDatabase.closeConn(conn);
		}	
	}
	
	public static Date checkPageModifiedDate(String url) {
		logger.debug("checkPageModifiedDate method is called");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Date date = null;
		
		String sql = "select RSPageModifiedDate from RSPage where url = '" + url + "'";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			stmt = RSMySQLDatabase.getStatement(conn);
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			while(rs.next()) {
				date = rs.getTimestamp("RSPageModifiedDate");
			}
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeStatement(stmt);
			RSMySQLDatabase.closeConn(conn);
		}	
		return date;
	}
	
	public static Date checkPageLastAccessDate(String url) {
		logger.debug("checkPageLastAccessDate method is called");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Date date = null;
		
		String sql = "select RSPageLastAccessDate from RSPage where url = '" + url + "'";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			stmt = RSMySQLDatabase.getStatement(conn);
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			while(rs.next()) {
				date = rs.getTimestamp("RSPageLastAccessDate");
			}
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeStatement(stmt);
			RSMySQLDatabase.closeConn(conn);
		}	
		return date;
	}
	
	public static int[] checkPageReferenceInformation(String url) {
		logger.debug("checkPageInformation method is called");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int[] countArray = {0, 0, 0, 0};
		
		String sql = "select characterCount, wordCount, tagCount, lineCount from RSPage where url = '" + url + "'";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			stmt = RSMySQLDatabase.getStatement(conn);
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			while(rs.next()) {
				countArray[0] = rs.getInt("characterCount");
				countArray[1] = rs.getInt("wordCount");
				countArray[2] = rs.getInt("tagCount");
				countArray[3] = rs.getInt("lineCount");
			}
		} catch (SQLException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeStatement(stmt);
			RSMySQLDatabase.closeConn(conn);
		}	
		return countArray;
	}
	
	public static void updatePageModifiedDate(String url, Date newDate) {
		//PropertyConfigurator.configure("log4j.properties");
		logger.debug("updatePageModifiedDate method is called");
		Connection conn = null;
		PreparedStatement pStmt = null;
		String sql = null;
		
		try {
			conn = RSMySQLDatabase.getConnection();
			sql = "update RSPage set RSPageModifiedDate=? where url = '" + url + "'";
			pStmt = RSMySQLDatabase.getPreparedStatment(conn, sql);
			pStmt.setTimestamp(1, new Timestamp(newDate.getTime()));
			pStmt.executeUpdate();			
		} catch (SQLException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeStatement(pStmt);
			RSMySQLDatabase.closeConn(conn);
		}	
	}
	
	public static void updatePageLastAccessDate(String url, Date newDate) {
		//PropertyConfigurator.configure("log4j.properties");
		logger.debug("updatePageLastAccessDate method is called");
		Connection conn = null;
		PreparedStatement pStmt = null;
		String sql = null;
		
		try {
			conn = RSMySQLDatabase.getConnection();
			sql = "update RSPage set RSPageLastAccessDate=? where url = '" + url + "'";
			pStmt = RSMySQLDatabase.getPreparedStatment(conn, sql);
			pStmt.setTimestamp(1, new Timestamp(newDate.getTime()));
			pStmt.executeUpdate();			
		} catch (SQLException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeStatement(pStmt);
			RSMySQLDatabase.closeConn(conn);
		}	
	}
	
	public static void updatePageReferenceInformation(String url, int characterCount, int wordCount, int tagCount, int lineCount) {
		//PropertyConfigurator.configure("log4j.properties");
		logger.debug("updatePageInformation method is called");
		Connection conn = null;
		PreparedStatement pStmt = null;
		String sql = null;
		
		try {
			conn = RSMySQLDatabase.getConnection();
			sql = "update RSPage set characterCount=?, wordCount=?, tagCount=?, lineCount=? where url = '" + url + "'";
			pStmt = RSMySQLDatabase.getPreparedStatment(conn, sql);
			pStmt.setInt(1, characterCount);
			pStmt.setInt(2, wordCount);
			pStmt.setInt(3, tagCount);
			pStmt.setInt(4, lineCount);
			pStmt.executeUpdate();			
		} catch (SQLException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeStatement(pStmt);
			RSMySQLDatabase.closeConn(conn);
		}	
	}
	
	public static boolean existPageId(String url) {
		logger.debug("exists method is called.");
		Connection conn = null;
		ResultSet rs = null;
		
		String sql = "select rspageid from RSPage where url = '" + url + "'";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			if(rs.next()) {
				return true;
			} 
		} catch (SQLException e) {
			//PropertyConfigurator.configure("log4j.properties");
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}	
		return false;
	}
	
	public static ArrayList<Integer> pageIdList() {
		logger.debug("totalNumberOfPages method is called");
		Connection conn = null;
		ResultSet rs = null;
		ArrayList<Integer> pageIdList = new ArrayList<Integer>();
		
		String sql = "select RSPageId from RSPage order by RSPageId";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			while(rs.next()) {
				pageIdList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}	
		return pageIdList;
	}

	public static String searchURLByPageId (int RSPageId) {
		logger.debug("searchURLByPageId method is called");
		Connection conn = null;
		ResultSet rs = null;
		String url = null;
		
		String sql = "select url from RSPage where RSPageId = " + RSPageId;
		
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			if(rs.next()) {
				url = rs.getString("url");
			} 
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}	
		return url;
	}

	public static ArrayList<String> allPageListInDb() {
		ArrayList<String> pageInRSPage = new ArrayList<String>();
		logger.debug("searchURLByPageId method is called");
		Connection conn = null;
		ResultSet rs = null;
		
		String sql = "select url from RSPage";
		
		try {
			conn = RSMySQLDatabase.getConnection();
			rs = RSMySQLDatabase.executeQuery(conn, sql);
			
			while(rs.next()) {
				pageInRSPage.add(rs.getString("url"));
			} 
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			RSMySQLDatabase.closeResultSet(rs);
			RSMySQLDatabase.closeConn(conn);
		}	
		return pageInRSPage;
	}

	public static void deleteNotExistPage(String url) {
		logger.debug("deleteFileInRSPage method is called");
		Connection conn = null;
		Statement stmt = null;
		
		String sql = "delete from RSPage where url = '" + url + "'";
		
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
