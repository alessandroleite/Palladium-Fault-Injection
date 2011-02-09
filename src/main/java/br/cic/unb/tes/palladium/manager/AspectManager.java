package br.cic.unb.tes.palladium.manager;

public class AspectManager {
	
	private static AspectManager manager;
	
	private LoadConfiguration configuration;
	
	private AspectManager() {
		configuration = new LoadConfiguration();
	}
	
	private synchronized static AspectManager initManager(){		
		if (manager == null){
			manager = new AspectManager();
			Thread t = new Thread(new FileMonitor());
			t.setDaemon(true);
			t.start();			
		}
		return manager;
	}
	
	
	public synchronized static AspectManager instance(){
		AspectManager manager = initManager();
		return manager;
	}
	
	public synchronized LoadConfiguration getConfiguration() {
		return configuration;
	}
}
