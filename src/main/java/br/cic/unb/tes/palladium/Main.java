package br.cic.unb.tes.palladium;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.zip.ZipException;

import org.jboss.aop.pointcut.ast.ParseException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import br.cic.unb.tes.palladium.options.PalladiumOptions;
import br.cic.unb.tes.palladium.util.ClassUtils;

public class Main {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws URISyntaxException, ZipException, IOException, ClassNotFoundException, ParseException {
		PalladiumOptions options = new PalladiumOptions();
		CmdLineParser parser = new CmdLineParser(options);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			parser.printUsage(System.out);
			System.exit(0);
		}
		if (options.isShowAspectList()){			
			List<Class> aspects = ClassUtils.getApplicationClasses("br.cic.unb.tes.palladium.interceptor");
			System.out.println("\tAvailable aspects:");
			for(Class clazz :  aspects){
				System.out.println("\t\t"+clazz.getCanonicalName());
			}
		} else {
			parser.printUsage(System.out);
			System.exit(0);
		}						
	}

}