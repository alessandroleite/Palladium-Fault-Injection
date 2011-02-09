package br.cic.unb.tes.palladium.options;

import org.kohsuke.args4j.Option;

/**
 * Palladium command line options definition
 * @author Vinicius Nunes 
 *
 */
public class PalladiumOptions {
	
	
//	@Option(name="-a",usage="Path to the target application",aliases="--path-to-app",metaVar="PATH_EXP")
//	public String appPath;
//	
//	@Option(name="-acp",usage="Classpath of the target application",aliases="--app-class-path",metaVar="PATH_EXP")
//	public String appClassPath;
//	
	@Option(name="-l",usage="See the list of available aspects",aliases="--list")
	public  boolean showAspectList;
//	
//	@Argument(usage="Selected aspects names",metaVar="aspect1 aspect2 ...")
//	public  List<String> arguments = new ArrayList<String>();

//	public String getAppPath() {
//		return appPath;
//	}
//
//	public void setAppPath(String appPath) {
//		this.appPath = appPath;
//	}
//
//	public String getAppClassPath() {
//		return appClassPath;
//	}
//
//	public void setAppClassPath(String appClassPath) {
//		this.appClassPath = appClassPath;
//	}

	public boolean isShowAspectList() {
		return showAspectList;
	}

	public void setShowAspectList(boolean showAspectList) {
		this.showAspectList = showAspectList;
	}

//	public List<String> getArguments() {
//		return arguments;
//	}
//
//	public void setArguments(List<String> arguments) {
//		this.arguments = arguments;
//	}
//	
	
			 
}
