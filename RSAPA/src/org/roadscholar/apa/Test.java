package org.roadscholar.apa;

public class Test {

	public static void main(String[] args) {
		
		FileProcessWebService webService = new FileProcessWebService();
		String rootPath = "z:\\wwwroot";
		webService.schedule(rootPath );
	}

}
