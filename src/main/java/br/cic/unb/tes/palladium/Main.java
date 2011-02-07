package br.cic.unb.tes.palladium;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.zip.ZipException;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.pointcut.ast.ParseException;

import br.cic.unb.tes.palladium.interceptor.FileInputStreamInterceptor;


@SuppressWarnings("rawtypes")
public class Main {
	
	
	public static void main(String[] args) throws URISyntaxException, ZipException, IOException, ClassNotFoundException, ParseException {
		AdviceBinding binding = new AdviceBinding("superpc","call(java.io.FileInputStream->new(..))", null);
		binding.addInterceptor(FileInputStreamInterceptor.class);
		AspectManager.instance().addBinding(binding);
		new Teste();
		
//		PalladiumOptions options = new PalladiumOptions();		
//		CmdLineParser parser = new CmdLineParser(options);		
//		try {
//			parser.parseArgument(args);
//		} catch (CmdLineException e) {
//			parser.printUsage(System.out);
//			System.exit(0);
//		}				
//		if (options.isShowAspectList() && (options.getAppPath() == null || options.getAppPath().isEmpty())){		
//			List<Class> aspects = ClassUtils.getApplicationClasses("br.cic.unb.tes.palladium.interceptor");
//			System.out.println("\tAvailable aspects:");
//			for(Class clazz :  aspects){
//				System.out.println("\t\t"+clazz.getCanonicalName());
//			}
//		} else if (!options.isShowAspectList() && (options.getAppPath() != null && !options.getAppPath().isEmpty())){
//			new ExecuteTask().aspectize(options.getAppPath(),options.getAppClassPath(),options.getArguments());
//		} else {
//			parser.printUsage(System.out);
//			System.exit(0);
//		}				
	}

}
