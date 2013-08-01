package org.roadscholar.apa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class RSASPFileProcess implements ASPFileProcess {

	public static Logger logger = Logger.getLogger(RSASPFileProcess.class);
	private RSPage rsp = new RSPage();
	private RSDirectory rsd = new RSDirectory();
	private RSAttribute rsa = new RSAttribute();
	private RSKeyword rsk = new RSKeyword();
	private RSPageAttributeAffiliation rspaa = new RSPageAttributeAffiliation();
	private RSPageKeywordAffiliation rspka = new RSPageKeywordAffiliation();
	private boolean rootFlag = true;
	private int RSDirectoryId = 0;
	public int countASP = 0;
	private ArrayList<String> pageList = new ArrayList<String>();
		
	@Override
	public void addPageList(String rootPath) {
		File dir = null;
		Date fileCreationDate = null;
		Date fileModifiedDate = null;
		Date fileLastAccessDate = null;
		int parentId = 0;
		
		try {
		     dir = new File(rootPath);

		     if (!dir.exists())
		     {
		    	 logger.info("Directory was not found!");
		    	 System.exit(-1);
		     }
		} catch(SecurityException se) {
		     logger.error(se);
		}
		
		
		if(rootFlag == true) {
			rootFlag = false;
			
			if(!rsd.existDirectoryId(dir.getAbsolutePath().toLowerCase())){
				rsd.insertDirectoryList(dir.getName().toLowerCase(), dir.getAbsolutePath().toLowerCase(), 0);
			}
		}
		
		File[] file = dir.listFiles();
			
		if(file == null) {
			return;
		}
		
		for(int i=0; i<file.length; i++) {
			if(file[i].isDirectory()) {
				if(!rsd.existDirectoryId(file[i].getAbsolutePath().toLowerCase())) {
					parentId = rsd.getDirectoryId(file[i].getParent());
					rsd.insertDirectoryList(file[i].getName().toLowerCase(), file[i].getAbsolutePath().toLowerCase(), parentId);
				} 
				addPageList(file[i].getAbsolutePath());
			} else {				
				if(file[i].getName().toLowerCase().endsWith(".asp")) {
					pageList.add(file[i].getAbsolutePath().toLowerCase());
					if(!rsp.existPageId(file[i].getAbsolutePath().toLowerCase())) {			
						String absolutePath = file[i].getParent();
						RSDirectoryId = rsd.getDirectoryId(absolutePath);
						int characterCount = characterCount(file[i].getAbsolutePath().toLowerCase());
						int[] wordAndLineCount = wordAndLineCount(file[i].getAbsolutePath().toLowerCase());
						int tagCount = tagCount(file[i].getAbsolutePath().toLowerCase());
						fileCreationDate = fileCreationDate(file[i].getAbsolutePath().toLowerCase());
						fileModifiedDate = fileModifiedDate(file[i].getAbsolutePath().toLowerCase());
						fileLastAccessDate = fileLastAccessDate(file[i].getAbsolutePath().toLowerCase());
						rsp.insertPageList(file[i].getName().toLowerCase(), null, file[i].getAbsolutePath().toLowerCase(), 
								RSDirectoryId, characterCount, wordAndLineCount[0], tagCount, wordAndLineCount[1],
								fileCreationDate, fileModifiedDate, fileLastAccessDate
								);
						countASP++;
						logger.info("The " + countASP + " page is added!");
						
					}	else {
						//update characterCount, wordCount, tagCount, lineCount of the file
						updatePageReferenceInformation(file[i].getAbsolutePath().toLowerCase());
						
						//update modified date of the file
						updatePageModifiedDate(file[i].getAbsolutePath().toLowerCase());
						
						//update last access date of the file
						updatePageLastAccessDate(file[i].getAbsolutePath().toLowerCase());
						countASP++;
						logger.info("The " + countASP + " page is updated!");
						
					}
				}
			}
		}
		
	}

	@Override
	public void deleteNotExistPage() {
		logger.debug("The deleteNotExistPage method is called");
		ArrayList<String> pageListInDb = new ArrayList<String>();
		Iterator<String> itFileInDb = null;
		String str = null;

		pageListInDb = rsp.allPageListInDb();
		pageListInDb.removeAll(pageList);
		
		itFileInDb = pageListInDb.iterator();
		while(itFileInDb.hasNext()) {
			str = itFileInDb.next();
			rsp.deleteNotExistPage(str);
			logger.info("The " + str + " page is deleted!");
		}
	}
	
	@Override
	public void addPageAttributeAffiliationList() {
		logger.debug("The addPageAttributeAffiliationList method is called");
		String pageURL = null;
		String pageContent = null;
		ArrayList<String> attributeInPageArrayList = null;
		ArrayList<String> pageAttributeAffiliationDescription = null;
		Iterator<String> itAttributeInPage = null;
		
		ArrayList<Integer> pageIdList = new ArrayList<Integer>();
		pageIdList = pageIdList();
		Iterator<Integer> itPageId = pageIdList.iterator();
		int pageId = 0;
		
		ArrayList<Integer> attributeIdList = new ArrayList<Integer>();
		attributeIdList = attributeIdList();
		
		int AttributeId = 0;
		
		attributeInPageArrayList = new ArrayList<String>();
		pageAttributeAffiliationDescription = new ArrayList<String>();
		while(itPageId.hasNext()) {
			pageId = itPageId.next();
			pageURL = searchURLByPageId(pageId);
			Iterator<Integer> itAttributeId = attributeIdList.iterator();
			if(pageURL != null) {
				pageContent = readPageContent(pageURL);
				logger.debug("The readPageContent method is called");
				
				while(itAttributeId.hasNext()) {
					AttributeId = itAttributeId.next();
					deletePageAttributeAffiliationList(pageId, AttributeId);
					attributeInPageArrayList = findAttributeInPage(AttributeId, pageContent, pageURL);
					logger.debug("The findAttributeInPage method is called");
					pageAttributeAffiliationDescription = rspaa.checkPageAttributeAffiliationDescription(pageId, AttributeId);
					attributeInPageArrayList.removeAll(pageAttributeAffiliationDescription);
										
					itAttributeInPage = attributeInPageArrayList.iterator();
					while(itAttributeInPage.hasNext()) {
						rspaa.insertPageAttributeAffiliationList(pageId, AttributeId, itAttributeInPage.next(), 0);
						logger.info("The " + pageId + " page and the " + AttributeId + " attribute is added!");
					}
				}
				logger.info("The " + pageId + " page's attribute description is updated!");
			}
		}
		
	}
	
	public void deletePageAttributeAffiliationList(int pageId, int attributeId) {
		logger.debug("The deletePageAttributeAffiliationList method is called");
		String pageURL = null;
		String pageContent = null;
		ArrayList<String> attributeInPageArrayList = null;
		ArrayList<String> pageAttributeAffiliationDescription = null;
		Iterator<String> itAttributeInPage = null;

		attributeInPageArrayList = new ArrayList<String>();
		pageAttributeAffiliationDescription = new ArrayList<String>();
		pageURL = searchURLByPageId(pageId);
		pageContent = readPageContent(pageURL);
		logger.debug("The readPageContent method is called");
			
		attributeInPageArrayList = findAttributeInPage(attributeId, pageContent, pageURL);
		logger.debug("The findAttributeInPage method is called");
		pageAttributeAffiliationDescription = rspaa.checkPageAttributeAffiliationDescription(pageId, attributeId);
		pageAttributeAffiliationDescription.removeAll(attributeInPageArrayList);
		
		itAttributeInPage = pageAttributeAffiliationDescription.iterator();
		while(itAttributeInPage.hasNext()) {
			rspaa.deleteRSPageAttributeAffiliation(pageId, attributeId, itAttributeInPage.next());
			logger.info("The " + pageId + " page and the " + attributeId + " attribute is deleted!");
		}
	}
		
	@Override
	public void addPageKeywordAffiliationList() {
		logger.debug("The addPageAttributeAffiliationList method is called");
		
		String pageURL = null;
		String keywordName = null;
		
		ArrayList<Integer> pageIdList = new ArrayList<Integer>();
		pageIdList = pageIdList();
		
		ArrayList<Integer> keywordIdList = new ArrayList<Integer>();
		keywordIdList = keywordIdList();
		
		Iterator<Integer> itPageId = pageIdList.iterator();
		
		int keywordId = 0;
		int pageId = 0;
		
		while(itPageId.hasNext()) {
			pageId = itPageId.next();
			pageURL = searchURLByPageId(pageId);
			Iterator<Integer> itKeywordId = keywordIdList.iterator();
			if(pageURL != null) {					
				while(itKeywordId.hasNext()) {
					keywordId = itKeywordId.next();
					keywordName = searchKeywordNameByKeywordId(keywordId);
					insertPageKeywordAffiliation(pageId, keywordId, pageURL, keywordName);
				}	
				logger.info("The " + pageId + " page's keyword description is updated!");
			}
		}
		
	}
	
	//running program method
	@Override
	public void running(int flag, String rootPath) {
		long start = 0;
		long end = 0;
		
		start = System.currentTimeMillis();
		logger.info("The prcoess starts...");
		if(flag == 1) {
			addPageList(rootPath);
		} 
		if(flag == 2) {
			addPageAttributeAffiliationList();
		}
		if(flag == 3) {
			deleteNotExistPage();
		}
		if(flag == 4) {
			addPageKeywordAffiliationList();
		}
		logger.info("The process ends! ");
		end = System.currentTimeMillis();
		
		int seconds = (int) ((end-start) / 1000) % 60 ;
		int minutes = (int) (((end-start) / (1000*60)) % 60);
		
		logger.info("Total execute time is " + minutes + " minutes and " + seconds + " seconds.");
		
	}
	
	//check the character, word, tag, line information of a specified asp file
	@Override
	public int[] checkPageReferenceInformation(String url) {
		return rsp.checkPageReferenceInformation(url);
	}
	
	//check the modified date of a specified asp file
	@Override
	public Date checkPageModifiedDate(String url) {
		return rsp.checkPageModifiedDate(url);
	}
	
	//update the modified date of a specified asp file
	@Override
	public void updatePageModifiedDate(String url) {
		Date oldDate = checkPageModifiedDate(url);
		Date newDate = fileModifiedDate(url);
		
		if(newDate != oldDate) {
			rsp.updatePageModifiedDate(url, newDate);
		}
	}
	
	//update character, word, tag, line information of a specified asp file
	@Override
	public void updatePageReferenceInformation(String url) {
		int[] countArray = checkPageReferenceInformation(url);
		int characterCount = characterCount(url);
		int[] wordAndLineCount = wordAndLineCount(url);
		int tagCount = tagCount(url);
		if((characterCount != countArray[0]) || (wordAndLineCount[0] != countArray[1]) 
				|| (tagCount != countArray[2]) || (wordAndLineCount[1] != countArray[3])) {
			rsp.updatePageReferenceInformation(url, characterCount, 
					wordAndLineCount[0], tagCount, wordAndLineCount[1]);
		}
	}
	
	//check the last access date of a specified asp file
	@Override
	public Date checkPageLastAccessDate(String url) {	
		return rsp.checkPageLastAccessDate(url);
	}
	
	//update the last access date of a specified asp file
	@Override
	public void updatePageLastAccessDate(String url) {
		Date oldDate = checkPageLastAccessDate(url);
		Date newDate = fileLastAccessDate(url);
		
		if(newDate != oldDate) {
			rsp.updatePageLastAccessDate(url, newDate);
		}
	}
		
	//search the page URL by using pageId
	@Override
	public String searchURLByPageId (int RSPageId) {
		return rsp.searchURLByPageId (RSPageId);
	}
	
	@Override
	public String searchKeywordNameByKeywordId (int RSKeywordId) {
		return rsk.searchKeywordNameByKeywordId (RSKeywordId);
	}
	
	//search the attributeName by using attributeId
	@Override
	public String searchAttributeNameByAttributeId (int RSAttributeId) {
		return rsa.searchAttributeNameByAttributeId(RSAttributeId);
	}
	
	//count the number of attributes
	@Override
	public ArrayList<Integer> attributeIdList() {
		return rsa.attributeIdList();
	}
	
	//count the number of attributes
	@Override
	public ArrayList<Integer> keywordIdList() {
		return rsk.keywordIdList();
	}
	
	//count the number of pages
	@Override
	public ArrayList<Integer> pageIdList() {
		return rsp.pageIdList();
	}
	
	//check if the pageAttributeAffiliationDecription already exists in the pageAttributeAffiliation table
	@Override
	public ArrayList<String> checkPageAttributeAffiliationDescription(int RSPageId, int RSAttributeId) {
		return rspaa.checkPageAttributeAffiliationDescription(RSPageId, RSAttributeId);
	}

	//insert Page Keyword Affiliation
	private void insertPageKeywordAffiliation(int pageId, int keywordId, String pageURL, String keyword) {
		
		BufferedReader pageContent = null;
		
		try {
			pageContent = new BufferedReader(new FileReader(pageURL));
			int sector = 0;
			String readLineContent = null;
			
			String searchKeywordRegEx = "(?i)\\b" + keyword + "\\b";
			Pattern keywordPattern = null;
			Matcher matchKeyword = null;
			
			while((readLineContent = pageContent.readLine()) != null) {
				sector ++;
				keywordPattern = Pattern.compile(searchKeywordRegEx);
				matchKeyword = keywordPattern.matcher(readLineContent);
				
				while(matchKeyword.find()) {
					if(!rspka.existPageKeywordAffiliationDescription(pageId, keywordId, sector)) {
						rspka.insertRSPageKeywordAffiliation(pageId, keywordId, readLineContent.trim(), sector);
						logger.info("The " + pageId + " page and the " + keywordId + " keyword is added!");
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if(pageContent != null) {
				try {
					pageContent.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}
	
	//read the asp page content
	private String readPageContent(String path) {
	    StringBuffer line = new StringBuffer();
	    FileReader fileIn = null;
	    BufferedReader buffIn = null;
		try {
			fileIn = new FileReader(path);
			buffIn = new BufferedReader(fileIn);
			 
			while(buffIn.ready()) {
		        line.append(buffIn.readLine().trim() + " ");	        	
		    }
		} catch (FileNotFoundException e1) {
			logger.error(e1);
			e1.printStackTrace();
	    } catch(IOException e){
	    	logger.error(e);
	        e.printStackTrace();
	    } finally {
	    	if(fileIn != null) {
	    		try {
					fileIn.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
	    	}
	    	if(buffIn != null) {
	    		try {
					buffIn.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
	    	}
	    }
	    return line.toString();
	}
	
	private ArrayList<String> findIfStatementInPage(String fileName) {
		ArrayList<String> ifStatementInPage = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String tempStr = null;
			ifStatementInPage = new ArrayList<String>();
			String businessLogicRegExIf = "(?i)\\bif\\b\\s+(?!\\bend\\b)\\w+.*?\\bthen\\b";
			
			while((tempStr = br.readLine()) != null) {
				Pattern pattern = Pattern.compile(businessLogicRegExIf);
				Matcher matcher = pattern.matcher(tempStr);
				
				while(matcher.find()){
					ifStatementInPage.add(matcher.group());
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		return ifStatementInPage;
	}
	
	//find the content using a specified attribute (attributeId) in asp page 
	private ArrayList<String> findAttributeInPage(int attributeId, String content, String fileName) {
		ArrayList<String> attributeInPageArrayList = null;
		if(attributeId == 1) {
			attributeInPageArrayList = new ArrayList<String>();
			String urlRegEx = "(?i)<a\\s+([^>]+)>(.*?)?\\s*</a>";
			
			Pattern pattern = Pattern.compile(urlRegEx);
			Matcher ma = pattern.matcher(content);
				
			while(ma.find()) {
				attributeInPageArrayList.add(ma.group());
			}		
		}
		if(attributeId == 2) {
			attributeInPageArrayList = new ArrayList<String>();
			
			String businessLogicRegExFor = "(?i)(\\bfor\\b)\\s+(\\b(?!for)\\w+\\b)\\s*=\\s*(.*?)(\\bnext\\b)";
			String businessLogicRegExWhile = "(?i)\\bwhile\\b\\s+(.*?)\\bwend\\b";
			String businessLogicRegExDo = "(?i)\\bdo\\b\\s+[^not]+\\s+(.*?)\\bloop\\b";
			
			Pattern pattern = Pattern.compile(businessLogicRegExWhile);
			Matcher ma = pattern.matcher(content);
			
			while(ma.find()) {
				attributeInPageArrayList.add(ma.group());
			}	
			
			pattern = Pattern.compile(businessLogicRegExDo);
			ma = pattern.matcher(content);
			
			while(ma.find()) {
				attributeInPageArrayList.add(ma.group());
			}	
			
			pattern = Pattern.compile(businessLogicRegExFor);
			ma = pattern.matcher(content);
			
			while(ma.find()) {
				attributeInPageArrayList.add(ma.group());
			}	
			
			attributeInPageArrayList.addAll(findIfStatementInPage(fileName));
					
		}
		if(attributeId == 3) {
			attributeInPageArrayList = new ArrayList<String>();
			String imgRegEx = "(?i)<img\\s+(.+?)[^%]\\s*>";
			
			Pattern pattern = Pattern.compile(imgRegEx);
			Matcher ma = pattern.matcher(content);
				
			while(ma.find()) {
				attributeInPageArrayList.add(ma.group());
			}		
		}
		if(attributeId == 4) {
			attributeInPageArrayList = new ArrayList<String>();
			String pdfRegEx = "(?i)href\\s*=\\s*(\"([^\"]*\\.pdf\")|'[^']*\\.pdf')";
			
			Pattern pattern = Pattern.compile(pdfRegEx);
			Matcher ma = pattern.matcher(content);
				
			while(ma.find()) {
				attributeInPageArrayList.add(ma.group());
			}
		}
		if(attributeId == 5) {
			attributeInPageArrayList = new ArrayList<String>();
			String scriptRegEx = "<(?i)[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			
			Pattern pattern = Pattern.compile(scriptRegEx);
			Matcher ma = pattern.matcher(content);
				
			while(ma.find()) {
				if((ma.group().toLowerCase().contains("javascript\"")||
						(ma.group().toLowerCase().contains("javascript'")))) {
					attributeInPageArrayList.add(ma.group());
				}
			}	
		}
		if(attributeId == 6) {
			attributeInPageArrayList = new ArrayList<String>();
			String logInRequirementRegEx = "(?i)Call\\s*RequiresLogin\\s*\\((.*?)\\)";
			
			Pattern pattern = Pattern.compile(logInRequirementRegEx);
			Matcher ma = pattern.matcher(content);
				
			while(ma.find()) {
				attributeInPageArrayList.add(ma.group());
			}	
		}
		if(attributeId == 7) {
			attributeInPageArrayList = new ArrayList<String>();
			String scriptRegEx = "(?i)<!--\\s*#include(.+?)\\s*-->";
			
			Pattern pattern = Pattern.compile(scriptRegEx);
			Matcher ma = pattern.matcher(content);
				
			while(ma.find()) {
				attributeInPageArrayList.add(ma.group());
			}	
		}
		return attributeInPageArrayList;
	}
	
	//the last access date of a specified asp file
	private Date fileLastAccessDate(String path) {
		Path file = Paths.get(path);
		Date date = null;
        try {
			BasicFileAttributes attr =
			        Files.readAttributes(file, BasicFileAttributes.class);
	        date = new Date(attr.lastAccessTime().toMillis());
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return date; 
	}
	
	//the creation date of a specified asp file
	private Date fileCreationDate(String path) {
		Path file = Paths.get(path);
		Date date = null;
        try {
			BasicFileAttributes attr =
			        Files.readAttributes(file, BasicFileAttributes.class);
	        date = new Date(attr.creationTime().toMillis()); //attr.creationTime() returns a FileTime    
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return date;  
	}
	
	//the modified date of a specified asp file
	private Date fileModifiedDate(String path) {
		Path file = Paths.get(path);
		Date date = null;
        try {
			BasicFileAttributes attr =
			        Files.readAttributes(file, BasicFileAttributes.class);
	        date = new Date(attr.lastModifiedTime().toMillis());
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return date; 
	}
	
	//the total number of characters in a specified asp file
	private int characterCount(String ASPName) {
		int characterNumber = 0;
		File file = new File(ASPName);	
		InputStream is = null;
		
		try {
			is = new FileInputStream(file);
			
			while(is.read()!= -1) {
				characterNumber ++;			
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		return characterNumber;		
	}
	
	//the total number of words and lines in a specified asp file
	private int[] wordAndLineCount(String ASPName) {
		int[] wordAndLineNumber = {0,0};
		int wordNumber = 0;
		BufferedReader br = null;
		String[] words = null;
		File file = new File(ASPName);
		int line = 1;	
		try {
			br = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			while((tempString = br.readLine()) != null) {
				words = tempString.split("\\s+");
				wordNumber = wordNumber + words.length;
				wordAndLineNumber[1] = line++;
			}
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}

		wordAndLineNumber[0] = wordNumber;
		
		return wordAndLineNumber;		
	}
	
	//the total number of tags in a specified asp file
	private int tagCount(String ASPName) {
		BufferedReader br = null;
		File file = new File(ASPName);
		String regEx = "<[^<|^>]+>"; 
		int tagNumber = 0;
		
		try {
			br = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			while((tempString = br.readLine()) != null) {
				Pattern pattern = Pattern.compile(regEx);
				Matcher ma = pattern.matcher(tempString);
				while(ma.find()) {
					tagNumber ++;
				}
			}
			
		} catch (FileNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		
		return tagNumber;
	}
}
