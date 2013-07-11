package org.roadscholar.apa;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FileProcessWebService {

	private final static long ONCE_PER_DAY = 1000*60*60*24;

	public void schedule(String rootPath) {

		MyTimerTask task = new MyTimerTask(rootPath);
			
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.HOUR_OF_DAY, 2);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		Date date = calendar.getTime();
		Timer timer = new Timer();  
        timer.schedule(task, date, ONCE_PER_DAY);

	}
	
	private class MyTimerTask extends TimerTask{
		private String rootPath = null;
		
		MyTimerTask(String rootPath) {
			this.rootPath = rootPath;
		}
		
		@Override
		public void run() {	
				ASPFileProcess fileProcess = new RSASPFileProcess();				
				fileProcess.running(1, rootPath);				
				fileProcess.running(2, rootPath);		
		}
	}
}
