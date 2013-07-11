package org.roadscholar.apa;

import java.util.ArrayList;
import java.util.Date;

public interface ASPFileProcess {
	public void addPageList(String rootPath);
	
	public void addPageAttributeAffiliationList();
	
	public void deletePageAttributeAffiliationList(int pageId, int attributeId);
	
	public int[] checkPageInformation(String url);
	
	public void updatePageInformation(String url);
	
	public Date checkPageModifiedDate(String url);
	
	public void updatePageModifiedDate(String url);
	
	public Date checkPageLastAccessDate(String url);
	
	public void updatePageLastAccessDate(String url);
	
	public int totalNumberOfPages();

	public String searchURLByPageId(int RSPageId);
	
	public int totalNumberOfAttribute();

	public String searchAttributeNameByAttributeId(int RSAttributeId);
	
	public void running(int flag, String rootPath);

	public ArrayList<String> checkPageAttributeAffiliationDescription(int RSPageId, int RSAttributeId);
	
	//public void checkAttribute();
	//public void addAttribute(String RSAttributeName, String RSAttributeDescription);
	//public void updateAttribute();
	
	//public void log();
}
