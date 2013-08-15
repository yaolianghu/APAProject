package org.roadscholar.apa;

public class Run {

	public static void main(String[] args) {
		//FileProcessWebService webService = new FileProcessWebService();
		String rootPath = "\\\\testweb\\Inetpub\\wwwroot";
		//webService.schedule(rootPath );
		
		ASPFileProcess fileProcess = new RSASPFileProcess();
		fileProcess.running(4, rootPath);
	}

}
