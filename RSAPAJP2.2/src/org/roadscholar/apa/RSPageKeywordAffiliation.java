package org.roadscholar.apa;

import org.roadscholar.apa.dao.RSPageKeywordAffiliationDAO;

public class RSPageKeywordAffiliation {
	
	private int RSPageKeywordAffiliationId;
	private int RSPageId;
	private int RSKeywordId;
	private String RSPageKeywordAffiliationDescription;
	private int RSPageKeywordAffiliationSector;

	public int getRSPageKeywordAffiliationId() {
		return RSPageKeywordAffiliationId;
	}
	public void setRSPageKeywordAffiliationId(int RSPageKeywordAffiliationId) {
		this.RSPageKeywordAffiliationId = RSPageKeywordAffiliationId;
	}
	public int getRSPageId() {
		return RSPageId;
	}
	public void setRSPageId(int RSPageId) {
		this.RSPageId = RSPageId;
	}
	public int getRSKeywordId() {
		return RSKeywordId;
	}
	public void setRSKeywordId(int RSKeywordId) {
		this.RSKeywordId = RSKeywordId;
	}
	public String getRSPageKeywordAffiliationDescription() {
		return RSPageKeywordAffiliationDescription;
	}
	public void setRSPageKeywordAffiliationDescription(
			String RSPageKeywordAffiliationDescription) {
		this.RSPageKeywordAffiliationDescription = RSPageKeywordAffiliationDescription;
	}
	public int getRSPageKeywordAffiliationSector() {
		return RSPageKeywordAffiliationSector;
	}
	public void setRSPageKeywordAffiliationSector(int RSPageKeywordAffiliationSector) {
		this.RSPageKeywordAffiliationSector = RSPageKeywordAffiliationSector;
	}

	public void insertRSPageKeywordAffiliation(RSPageKeywordAffiliation rspka) {
		RSPageKeywordAffiliationDAO.insertPageKeywordAffiliation(rspka);
	}
	
	public void insertRSPageKeywordAffiliation(int RSPageId, int RSKeywordId, 
			String RSPageKeywordAffiliationDescription, int RSPageKeywordAffiliationSector) {
		RSPageKeywordAffiliation rspka = new RSPageKeywordAffiliation();
		rspka.setRSPageId(RSPageId);
		rspka.setRSKeywordId(RSKeywordId);
		rspka.setRSPageKeywordAffiliationDescription(RSPageKeywordAffiliationDescription);
		rspka.setRSPageKeywordAffiliationSector(RSPageKeywordAffiliationSector);
		rspka.insertRSPageKeywordAffiliation(rspka);
	}
	
	public boolean existPageKeywordAffiliationDescription(int RSPageId, int RSKeywordId,
			int RSPageKeywordAffiliationSector) {
		return RSPageKeywordAffiliationDAO.existPageKeywordAffiliationDescription(RSPageId, RSKeywordId, 
			RSPageKeywordAffiliationSector);
	}
	
	
}
