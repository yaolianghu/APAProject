package org.roadscholar.apa;

import java.util.ArrayList;

import org.roadscholar.apa.dao.RSPageAttributeAffiliationDAO;

public class RSPageAttributeAffiliation {
	private int RSPageAttributeAffiliationId;
	private int RSPageId;
	private int RSAttributeId;
	private String RSPageAttributeAffiliationDescription;
	private int RSPageAttributeAffiliationSector;
	
	public int getRSPageAttributeAffiliationId() {
		return RSPageAttributeAffiliationId;
	}
	public void setRSPageAttributeAffiliationId(int RSPageAttributeAffiliationId) {
		this.RSPageAttributeAffiliationId = RSPageAttributeAffiliationId;
	}
	public int getRSPageId() {
		return RSPageId;
	}
	public void setRSPageId(int RSPageId) {
		this.RSPageId = RSPageId;
	}
	public int getRSAttributeId() {
		return RSAttributeId;
	}
	public void setRSAttributeId(int RSAttributeId) {
		this.RSAttributeId = RSAttributeId;
	}
	public String getRSPageAttributeAffiliationDescription() {
		return RSPageAttributeAffiliationDescription;
	}
	public void setRSPageAttributeAffiliationDescription(
			String RSPageAttributeAffiliationDescription) {
		this.RSPageAttributeAffiliationDescription = RSPageAttributeAffiliationDescription;
	}
	public int getRSPageAttributeAffiliationSector() {
		return RSPageAttributeAffiliationSector;
	}
	public void setRSPageAttributeAffiliationSector(
			int RSPageAttributeAffiliationSector) {
		this.RSPageAttributeAffiliationSector = RSPageAttributeAffiliationSector;
	}
	
	public void insertPageAttributeAffiliationList(RSPageAttributeAffiliation rspaa) {
		RSPageAttributeAffiliationDAO.insertPageAttributeAffiliationList(rspaa);
	}
	
	public void insertPageAttributeAffiliationList(int RSPageId, int RSAttributeId, 
			String RSPageAttributeAffiliationDescription, int RSPageAttributeAffiliationSector) {
		RSPageAttributeAffiliation rspaa = new RSPageAttributeAffiliation();
		rspaa.setRSPageId(RSPageId);
		rspaa.setRSAttributeId(RSAttributeId);
		rspaa.setRSPageAttributeAffiliationDescription(RSPageAttributeAffiliationDescription);
		rspaa.setRSPageAttributeAffiliationSector(RSPageAttributeAffiliationSector);
		rspaa.insertPageAttributeAffiliationList(rspaa);
	}
	
	public ArrayList<String> checkPageAttributeAffiliationDescription(int RSPageId, int RSAttributeId) {
		return RSPageAttributeAffiliationDAO.checkPageAttributeAffiliationDescription(RSPageId, RSAttributeId);
	}
	
	public void deleteRSPageAttributeAffiliation(int RSPageId, int RSAttributeId, 
			String RSPageAttributeAffiliationDescription) {
		if(RSPageAttributeAffiliationDescription.contains("'")) {				
			RSPageAttributeAffiliationDescription = RSPageAttributeAffiliationDescription.replace("'", "\\'");		
		} 
		RSPageAttributeAffiliationDAO.deleteRSPageAttributeAffiliation(RSPageId, RSAttributeId, 
				RSPageAttributeAffiliationDescription);		
	}
	
}
