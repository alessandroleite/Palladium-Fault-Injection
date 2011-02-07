package br.cic.unb.tes.palladium.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Compile {

	static void compile(String jarOrDirectoryToCompile, String libDir, String aopPath, String classPath) throws Exception {

		org.jboss.aop.standalone.Compiler compiler = new org.jboss.aop.standalone.Compiler();
		List<String> args = new ArrayList<String>();

		if (aopPath != null && !aopPath.trim().isEmpty())
			System.setProperty("jboss.aop.path", aopPath);

		if (classPath != null && classPath.trim().isEmpty())
			System.setProperty("jboss.aop.class.path", classPath);

		String[] files = new File(libDir).list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});

		StringBuilder cp = new StringBuilder();
		for (int i = 0; i < files.length; i++) {
			cp.append(libDir).append(files[i]);
			if (i < files.length - 1)
				cp.append(":");
		}

		args.add("-verbose");
		args.add("-verbose");
		args.add("-cp");
		args.add(cp.toString());

		args.add(jarOrDirectoryToCompile);
		compiler.compile(args.toArray(new String[args.size()]));

	}
}
