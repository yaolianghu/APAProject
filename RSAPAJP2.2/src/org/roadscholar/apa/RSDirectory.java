package org.roadscholar.apa;

import org.roadscholar.apa.dao.RSDirectoryDAO;

public class RSDirectory {
	private int RSDirectoryId;
	private String RSDirectoryName;
	private String absolutePath;
	private int parentId;
	
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getRSDirectoryId() {
		return RSDirectoryId;
	}
	public void setRSDirectoryId(int RSDirectoryId) {
		this.RSDirectoryId = RSDirectoryId;
	}
	public String getRSDirectoryName() {
		return RSDirectoryName;
	}
	public void setRSDirectoryName(String RSDirectoryName) {
		this.RSDirectoryName = RSDirectoryName;
	}
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	public void insertDirectoryList(String RSDirectoryName, String absolutPath, int parentId) {
		RSDirectoryDAO.insertDirectoryList(RSDirectoryName, absolutPath, parentId);
	}
	
	public int getDirectoryId(String absolutePath) {
		return RSDirectoryId = RSDirectoryDAO.getDirectoryId(absolutePath);
	}
	
	public boolean existDirectoryId(String absolutePath) {
		absolutePath = absolutePath.replace("\\", "\\\\");
		
		if(absolutePath.contains("'")) {				
			absolutePath = absolutePath.replace("'", "\\'");		
		} 
		
		return RSDirectoryDAO.existDirectoryId(absolutePath);
	}
}
