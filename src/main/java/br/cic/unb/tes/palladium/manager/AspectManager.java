package br.cic.unb.tes.palladium.manager;

public class AspectManager {
	
	private static AspectManager manager;
	
	private LoadConfiguration configuration;
	
	private synchronized static AspectManager initManager(){
		if (manager == null){
			new Thread(new FileMonitor()).start();
		}
		return manager;
	}
	
	
	public static AspectManager instance(){
		AspectManager manager = initManager();
		return manager;
	}
	
	public LoadConfiguration getConfiguration() {
		return configuration;
	}
}
