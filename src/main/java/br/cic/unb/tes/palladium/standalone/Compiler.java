package br.cic.unb.tes.palladium.standalone;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javassist.bytecode.ClassFile;

import org.jboss.aop.AspectManager;

public class Compiler {

	public boolean verbose = false;
	public boolean suppress = true;
	public boolean optimized = true;

	public boolean isJarFile(File src) {
		return (src.isFile() && (src.getName().toLowerCase().endsWith(".jar") || src
				.getName().toLowerCase().endsWith(".zip")));
	}

	// Make public and static so that transformers can locate it to do work
	// transformers may generate class files and they need to determine
	// file locations and such. This will also be used as a flag to tell
	// transformers whether they are in compile or load-time mode.
	public static URLClassLoader loader;

	// private HashMap<String, CompilerClassInfo> classesToCompile = new
	// HashMap<String, CompilerClassInfo>();

	public ClassFile createClassFile(final File file) throws Exception {
		DataInputStream is = new DataInputStream(new FileInputStream(file));
		ClassFile cf = new ClassFile(is);
		is.close();
		return cf;
	}

	/**
	 * Loads the file and, if it is an advised class, sets its advisor field as
	 * accessible.
	 * 
	 * @param file
	 *            the file of the class to be loaded
	 * @return {@code true} is {@code file} contains an advised class.
	 */
	public boolean loadFile(File file) throws Exception {
		DataInputStream is = new DataInputStream(new FileInputStream(file));
		ClassFile cf = new ClassFile(is);
		is.close();
		Class<?> clazz = loader.loadClass(cf.getName());
		if (org.jboss.aop.Advised.class.isAssignableFrom(clazz)) {
			Field f = clazz.getDeclaredField("aop$classAdvisor$aop");
			f.setAccessible(true);
			f.get(null);
			return true;
		}
		return false;
	}

	public byte[] compileClass(String classname, String classPath) throws Exception {
		
		List<URL> paths = new ArrayList<URL>();
		
		if (classPath != null){
		
		 StringTokenizer tokenizer = new StringTokenizer(classPath, File.pathSeparator);
         while (tokenizer.hasMoreTokens())
         {
            String cpath = tokenizer.nextToken();
            File f = new File(cpath);
            paths.add(f.toURI().toURL());
         }
		}
		
		URL[] urls = paths.toArray(new URL[paths.size()]);
	    loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());

	    Thread.currentThread().setContextClassLoader(loader);

		// if (info.getSuperClassName() != null) {
		// CompilerClassInfo superInfo =
		// classesToCompile.get(info.getSuperClassName());
		// if (superInfo != null) {
		// compileFile(superInfo);
		// }
		// }

		URL classUrl = loader.getResource(classname.replace('.', '/') + ".class");
		if (classUrl == null) {
			System.out
					.println("[warning] Unable to find "
							+ classname
							+ " within classpath.  Make sure all transforming classes are within classpath.");
			return null;
		}

		File f = new File(classname).getCanonicalFile();
		File classUrlFile = new File(URLDecoder.decode(classUrl.getFile(),"UTF-8"));
		File infoFile = new File(URLDecoder.decode(f.toString(), "UTF-8"));

		if (!classUrlFile.equals(infoFile)) {
			System.out.println("[warning] Trying to compile " + f
					+ " and found it also within " + classUrl.getFile()
					+ " will not proceed. ");
			return null;
		}
		byte[] bytes = AspectManager.instance().transform(loader, classname, null, null, null);
		if (bytes == null) {
			if (verbose)
				System.out.println("[no comp needed] " + f);
			return bytes;
		}
		FileOutputStream os = new FileOutputStream(infoFile);
		os.write(bytes);
		os.close();
		
		if (verbose)
			System.out.println("[compiled] " + f);
		return bytes;
	}
}