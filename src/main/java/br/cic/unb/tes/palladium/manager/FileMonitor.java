package br.cic.unb.tes.palladium.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class FileMonitor implements Runnable {
	
	private static final long SLEEP_TIME = 1000l;//one second
	private static final String FILE_NAME_KEY = "palladium.scanfile";
	private static final String DEFAULT_FILE_NAME = "palladium-runtime.config";
	
	public void run() {
		final String fileName = System.getProperty(FILE_NAME_KEY, DEFAULT_FILE_NAME);		
		long lastModifiedTime = 0l;
		while(true){			
			try {
				long currentModifiedTime = checkForModification(fileName); 
				if (currentModifiedTime != lastModifiedTime){
					Properties properties = new Properties();
					FileInputStream fin =  new FileInputStream(new File(fileName));
					properties.load(fin);					
					fin.close();
					AspectManager.instance().getConfiguration().applyConfiguration(properties);
				}
				lastModifiedTime = currentModifiedTime;
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException ignore) {
				
			} catch (IOException e) {
				throw new FileMonitorException("Scanfile Error", e);				
			}
		}
	}
	
	
	private long checkForModification(String fileName) throws IOException{
		File scanFile = new File(fileName);
		if (!scanFile.exists()){
			scanFile.createNewFile();
			return 0l;
		} else if (!scanFile.canRead()){
			throw new FileMonitorException("Scan file can not be read.");
		} else if (!scanFile.isFile()){
			throw new FileMonitorException("Invalid scan file.");
		} else {
			return scanFile.lastModified();
		}
	}
	
	public class FileMonitorException extends RuntimeException{

		private static final long serialVersionUID = -1122954555834923178L;

		public FileMonitorException(String msg, Throwable t) {
			super(msg, t);
		}
		
		public FileMonitorException(String msg) {
			super(msg);
		}
		
	}
	
}
