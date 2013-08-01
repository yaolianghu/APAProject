package org.roadscholar.apa;

import java.util.ArrayList;
import java.util.Date;

import org.roadscholar.apa.dao.RSAttributeDAO;

public class RSAttribute {
	private int RSAttributeId;
	private String RSAttributeName;
	private String RSAttributeDescription;
	private Date RSAttributeCreateDate;
	private Date RSAttributeModifiedDate;
	
	public int getRSAttributeId() {
		return RSAttributeId;
	}
	public void setRSAttributeId(int RSAttributeId) {
		this.RSAttributeId = RSAttributeId;
	}
	public String getRSAttributeName() {
		return RSAttributeName;
	}
	public void setRSAttributeName(String RSAttributeName) {
		this.RSAttributeName = RSAttributeName;
	}
	public String getRSAttributeDescription() {
		return RSAttributeDescription;
	}
	public void setRSAttributeDescription(String RSAttributeDescription) {
		this.RSAttributeDescription = RSAttributeDescription;
	}
	public Date getRSAttributeCreateDate() {
		return RSAttributeCreateDate;
	}
	public void setRSAttributeCreateDate(Date RSAttributeCreateDate) {
		this.RSAttributeCreateDate = RSAttributeCreateDate;
	}
	public Date getRSAttributeModifiedDate() {
		return RSAttributeModifiedDate;
	}
	public void setRSAttributeModifiedDate(Date RSAttributeModifiedDate) {
		this.RSAttributeModifiedDate = RSAttributeModifiedDate;
	}
	
	public void addAttribute(RSAttribute rsa) {
		RSAttributeDAO.addAttribute(rsa);
	}
	
	public void addAttribute(String RSAttributeName, String RSAttributeDescription, Date RSAttributeCreateDate, Date RSAttributeModifiedDate) {
		RSAttribute rsa = new RSAttribute();
		rsa.setRSAttributeName(RSAttributeName);
		rsa.setRSAttributeDescription(RSAttributeDescription);
		rsa.setRSAttributeCreateDate(RSAttributeCreateDate);
		rsa.setRSAttributeModifiedDate(RSAttributeModifiedDate);
		addAttribute(rsa);
	}
	
	public ArrayList<Integer> attributeIdList() {
		return RSAttributeDAO.attributeIdList();
	}
	
	public String searchAttributeNameByAttributeId (int RSPageId) {
		return RSAttributeDAO.searchAttributeNameByAttributeId(RSPageId);
	}
}
