package org.roadscholar.apa;

//import java.util.Calendar;
//import java.util.Date;
//import java.util.Timer;
//import java.util.TimerTask;

//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

//import org.roadscholar.log.RSDAOLog;

//import org.roadscholar.apa.bug.FindASPBug;

public class testGetFileName {

	//private final static long ONCE_PER_DAY = 1000*60*60*24;
	//static Logger logger = Logger.getLogger(testGetFileName.class);
	public static void main(String[] args) {
	
		ASPFileProcess fp = new RSASPFileProcess();
		String rootPath = "z:\\wwwroot";
		
		fp.running(1, rootPath);
		
		//fp.running(2, rootPath);

	}

}
