package org.roadscholar.apa;

import java.util.ArrayList;
import java.util.Date;

public interface ASPFileProcess {
	public void addPageList(String rootPath);
	
	public void deleteNotExistPage();
	
	public void addPageAttributeAffiliationList();
	
	public void deletePageAttributeAffiliationList(int pageId, int attributeId);
	
	public int[] checkPageReferenceInformation(String url);
	
	public void updatePageReferenceInformation(String url);
	
	public Date checkPageModifiedDate(String url);
	
	public void updatePageModifiedDate(String url);
	
	public Date checkPageLastAccessDate(String url);
	
	public void updatePageLastAccessDate(String url);
	
	public ArrayList<Integer> pageIdList();

	public String searchURLByPageId(int RSPageId);
	
	public ArrayList<Integer> attributeIdList();

	public String searchAttributeNameByAttributeId(int RSAttributeId);
	
	public void running(int flag, String rootPath);

	public ArrayList<String> checkPageAttributeAffiliationDescription(int RSPageId, int RSAttributeId);

	void addPageKeywordAffiliationList();

	String searchKeywordNameByKeywordId(int RSKeywordId);

	ArrayList<Integer> keywordIdList();

	
	
	//public void checkAttribute();
	//public void addAttribute(String RSAttributeName, String RSAttributeDescription);
	//public void updateAttribute();
	
	//public void log();
}
