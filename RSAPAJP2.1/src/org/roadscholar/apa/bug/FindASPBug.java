package org.roadscholar.apa.bug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindASPBug {

	public void checkURLBug(String rootPath) {
		//String pageURL = null;
		String pageContent = null;
		ArrayList<String> attributeInPageArrayList = null;
		//ArrayList<String> pageAttributeAffiliationDescription = null;
		Iterator<String> itAttributeInPage = null;
		String bugURL = null;
		File dir = new File(rootPath);

		File[] file = dir.listFiles();
		
		
		if(file == null) {
			return;
		}
		
		for(int i=0; i<file.length; i++) {
			if(file[i].isDirectory()) {
				checkURLBug(file[i].getAbsolutePath());
			} else {				
				if(file[i].getName().toLowerCase().endsWith(".asp")) {
					String fileName = file[i].getAbsolutePath().toLowerCase();
					System.out.println(fileName);

					attributeInPageArrayList = new ArrayList<String>();
					//pageAttributeAffiliationDescription = new ArrayList<String>();
						
					pageContent = readPageContent(fileName);
							
					attributeInPageArrayList = findAttributeInPage(1, pageContent);						
								
					itAttributeInPage = attributeInPageArrayList.iterator();
					while(itAttributeInPage.hasNext()) {
						bugURL = itAttributeInPage.next();
						if(bugURL.length()>700) {
							System.out.println(bugURL);
							logFile(fileName , "D:\\logFile.txt");
							logFile("bugs: ", "D:\\logFile.txt");
							logFile(bugURL, "D:\\logFile.txt");
							logFile(System.getProperty("line.separator"), "D:\\logFile.txt");
						}
					}	
					System.out.println();
				}
			}
		}
	}
	
	//read the asp page content
		private String readPageContent(String path) {
		    StringBuffer line = new StringBuffer();
		    FileReader fileIn = null;
		    BufferedReader buffIn = null;
		  //  int lineNumber = 1;
			try {
				fileIn = new FileReader(path);
				buffIn = new BufferedReader(fileIn);
				 
				while(buffIn.ready()) {
					//line.append("lineNumber: " + lineNumber + "  ");
					line.append(buffIn.readLine().trim() + " ");	
					//lineNumber++;
			    }
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
		    } catch(IOException e){
		        e.printStackTrace();
		    } finally {
		    	if(fileIn != null) {
		    		try {
						fileIn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    	}
		    	if(buffIn != null) {
		    		try {
						buffIn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    	}
		    }
		    return line.toString();
		}
		
		private ArrayList<String> findAttributeInPage(int attributeId, String content) {
			ArrayList<String> attributeInPageArrayList = null;
			if(attributeId == 1) {
				attributeInPageArrayList = new ArrayList<String>();
				//Updated the urlRegEx
				String urlRegEx = "(?i)<a\\s+([^>]+)>(.*?)?\\s*</a>";
				
				Pattern pattern = Pattern.compile(urlRegEx);
				Matcher ma = pattern.matcher(content);
					
				while(ma.find()) {
					attributeInPageArrayList.add(ma.group());
				}		
			}
			if(attributeId == 2) {
				attributeInPageArrayList = new ArrayList<String>();
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
			}
			if(attributeId == 5) {
				attributeInPageArrayList = new ArrayList<String>();
				String scriptRegEx = "(?i)<script\\s*(.+?)\\s*>(.+?)</script>";
				
				Pattern pattern = Pattern.compile(scriptRegEx);
				Matcher ma = pattern.matcher(content);
					
				while(ma.find()) {
					attributeInPageArrayList.add(ma.group());
				}	
			}
			if(attributeId == 6) {
				attributeInPageArrayList = new ArrayList<String>();
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
		
		public void logFile(String log, String fileName) {	
			try {
				FileWriter output = new FileWriter(fileName, true);
				output.write(log + System.getProperty("line.separator"));
				output.flush();
				output.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
