package org.roadscholar.apa;

import java.util.ArrayList;
import java.util.Date;

import org.roadscholar.apa.dao.RSKeywordDAO;

public class RSKeyword {

	private int RSKeywordId;
	private String RSKeywordName;
	private String RSKeywordDescription;
	private Date RSKeywordCreateDate;
	private Date RSKeywordModifiedDate;
	
	public int getRSKeywordId() {
		return RSKeywordId;
	}
	public void setRSKeywordId(int RSKeywordId) {
		this.RSKeywordId = RSKeywordId;
	}
	public String getRSKeywordName() {
		return RSKeywordName;
	}
	public void setRSKeywordName(String RSKeywordName) {
		this.RSKeywordName = RSKeywordName;
	}
	public String getRSKeywordDescription() {
		return RSKeywordDescription;
	}
	public void setRSKeywordDescription(String RSKeywordDescription) {
		this.RSKeywordDescription = RSKeywordDescription;
	}
	public Date getRSKeywordCreateDate() {
		return RSKeywordCreateDate;
	}
	public void setRSKeywordCreateDate(Date RSKeywordCreateDate) {
		this.RSKeywordCreateDate = RSKeywordCreateDate;
	}
	public Date getRSKeywordModifiedDate() {
		return RSKeywordModifiedDate;
	}
	public void setRSKeywordModifiedDate(Date RSKeywordModifiedDate) {
		this.RSKeywordModifiedDate = RSKeywordModifiedDate;
	}

	public ArrayList<Integer> keywordIdList() {
		return RSKeywordDAO.keywordIdList();
	}
	
	public String searchKeywordNameByKeywordId (int RSKeywordId) {
		return RSKeywordDAO.searchKeywordNameByKeywordId(RSKeywordId);
	}
}
