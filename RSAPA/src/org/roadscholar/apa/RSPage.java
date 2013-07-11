package org.roadscholar.apa;

//import java.sql.Timestamp;
import java.util.Date;

import org.roadscholar.apa.dao.RSPageDAO;

public class RSPage {

	private int RSPageId;
	private String RSPageName;
	private String note;
	private String url;
	private int RSDirectoryId;
	private int characterCount;
	private int wordCount;
	private int tagCount;
	private int lineCount;
	private Date RSPageCreateDate;
	private Date RSPageModifiedDate;
	private Date RSPageLastAccessDate;
	
	public int getRSPageId() {
		return RSPageId;
	}
	public void setRSPageId(int RSPageId) {
		this.RSPageId = RSPageId;
	}
	public String getRSPageName() {
		return RSPageName;
	}
	public void setRSPageName(String RSPageName) {
		this.RSPageName = RSPageName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getRSDirectoryId() {
		return RSDirectoryId;
	}
	public void setRSDirectoryId(int RSDirectoryId) {
		this.RSDirectoryId = RSDirectoryId;
	}
	public int getCharacterCount() {
		return characterCount;
	}
	public void setCharacterCount(int characterCount) {
		this.characterCount = characterCount;
	}
	public int getWordCount() {
		return wordCount;
	}
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	public int getTagCount() {
		return tagCount;
	}
	public void setTagCount(int tagCount) {
		this.tagCount = tagCount;
	}
	public int getLineCount() {
		return lineCount;
	}
	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}
	public Date getRSPageCreateDate() {
		return RSPageCreateDate;
	}
	public void setRSPageCreateDate(Date RSPageCreateDate) {
		this.RSPageCreateDate = RSPageCreateDate;
	}
	public Date getRSPageModifiedDate() {
		return RSPageModifiedDate;
	}
	public void setRSPageModifiedDate(Date RSPageModifiedDate) {
		this.RSPageModifiedDate = RSPageModifiedDate;
	}
	public Date getRSPageLastAccessDate() {
		return RSPageLastAccessDate;
	}
	public void setRSPageLastAccessDate(Date RSPageLastAccessDate) {
		this.RSPageLastAccessDate = RSPageLastAccessDate;
	}
	
	public void addPageList(RSPage rsp) {
		RSPageDAO.addPageFile(rsp);
	}
	
	public void addPageList(String RSPageName, String note, String url, 
			int RSDirectoryId, int characterCount, int wordCount, int tagCount, int lineCount,
			Date RSPageCreateDate, Date RSPageModifiedDate, Date RSPageLastAccessDate
			) {
		RSPage rsp = new RSPage();
		rsp.setRSPageName(RSPageName);
		rsp.setNote(note);
		rsp.setUrl(url);
		rsp.setRSDirectoryId(RSDirectoryId);
		rsp.setCharacterCount(characterCount);
		rsp.setWordCount(wordCount);
		rsp.setTagCount(tagCount);
		rsp.setLineCount(lineCount);
		rsp.setRSPageCreateDate(RSPageCreateDate);
		rsp.setRSPageModifiedDate(RSPageModifiedDate);
		rsp.setRSPageLastAccessDate(RSPageLastAccessDate);
		addPageList(rsp);
	}
	
	public void updatePageInformation(String url, int characterCount, 
			int wordCount, int tagCount, int lineCount) {
		url = url.replace("\\", "\\\\");
		if(url.contains("'")) {				
			url = url.replace("'", "\\'");		
		} 
		
		RSPageDAO.updatePageInformation(url, characterCount, wordCount, tagCount, lineCount);
	}
	
	public int[] checkPageInformation(String url) {
		url = url.replace("\\", "\\\\");
		if(url.contains("'")) {				
			url = url.replace("'", "\\'");		
		} 
		return RSPageDAO.checkPageInformation(url);
	}
	
	public Date checkPageModifiedDate(String url) {
		url = url.replace("\\", "\\\\");
		if(url.contains("'")) {				
			url = url.replace("'", "\\'");		
		} 
		return RSPageDAO.checkPageModifiedDate(url);
	}

	public void updatePageModifiedDate(String url, Date newDate) {
		url = url.replace("\\", "\\\\");
		if(url.contains("'")) {				
			url = url.replace("'", "\\'");		
		} 
		
		RSPageDAO.updatePageModifiedDate(url, newDate);
		
	}
	
	public Date checkPageLastAccessDate(String url) {
		url = url.replace("\\", "\\\\");
		if(url.contains("'")) {				
			url = url.replace("'", "\\'");		
		} 
		return RSPageDAO.checkPageLastAccessDate(url);
	}

	public void updatePageLastAccessDate(String url, Date newDate) {
		url = url.replace("\\", "\\\\");
		if(url.contains("'")) {				
			url = url.replace("'", "\\'");		
		} 
		
		RSPageDAO.updatePageLastAccessDate(url, newDate);
		
	}
	
	public boolean exists(String url) {
		url = url.replace("\\", "\\\\");
		if(url.contains("'")) {				
			url = url.replace("'", "\\'");		
		} 
		
		return RSPageDAO.exists(url);
	}
	
	public int totalNumberOfPages() {
		return RSPageDAO.totalNumberOfPages();
	}
	
	public String searchURLByPageId (int RSPageId) {
		return RSPageDAO.searchURLByPageId(RSPageId);
	}
}
