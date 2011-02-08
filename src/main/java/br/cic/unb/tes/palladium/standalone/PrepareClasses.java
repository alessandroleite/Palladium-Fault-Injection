package br.cic.unb.tes.palladium.standalone;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javassist.bytecode.ClassFile;

import org.jboss.aop.AspectManager;

import br.cic.unb.tes.palladium.util.ClassUtils;

public class PrepareClasses {
	
	public static URLClassLoader loader;

	private static final HashMap<String, CompilerClassInfo> classesToCompile = new HashMap<String, CompilerClassInfo>();

	private static FileFilter classFileFilter = new FileFilter() {
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".class");
		}
	};

	private static FileFilter directoryFilter = new FileFilter() {
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};

	
	public void prepare(String appPath) {
		List<URL> paths = new ArrayList<URL>();

		try {
			String appClassPath = System.getProperty("app.class.path", ClassUtils.getAppPath());
			StringTokenizer tokenizer = new StringTokenizer(appClassPath, File.pathSeparator);
			while (tokenizer.hasMoreTokens()) {
				String cpath = tokenizer.nextToken();
				File f = new File(cpath);
				paths.add(f.toURI().toURL());
			}

			URL[] urls = paths.toArray(new URL[paths.size()]);
			loader = new URLClassLoader(urls, Thread.currentThread()
					.getContextClassLoader());

			Thread.currentThread().setContextClassLoader(loader);

			File f = new File(appPath).getCanonicalFile();

			if (f.isDirectory()) {
				addDirectory(f);
			} else if (classFileFilter.accept(f)) {
				addFile(f);
			}
			
			try {
				for (String className : classesToCompile.keySet()) {
					CompilerClassInfo info = classesToCompile.get(className);
					compileFile(info);
				}
			} catch (Exception exception) {
				throw new RuntimeException(exception);
			}

		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}
	
	public void compileFile(CompilerClassInfo info) throws Exception {
		if (info.isCompiled()) {
			return;
		}

		if (info.getSuperClassName() != null) {
			CompilerClassInfo superInfo = classesToCompile.get(info
					.getSuperClassName());
			if (superInfo != null) {
				compileFile(superInfo);
			}
		}
		URL classUrl = loader.getResource(info.getClassName().replace('.', '/')
				+ ".class");
		if (classUrl == null) {
			System.out
					.println("[warning] Unable to find "
							+ info.getFile()
							+ " within classpath.  Make sure all transforming classes are within classpath.");
			return;
		}

		File classUrlFile = new File(URLDecoder.decode(classUrl.getFile(),
				"UTF-8"));
		File infoFile = new File(URLDecoder.decode(info.getFile().toString(),
				"UTF-8"));

		if (!classUrlFile.equals(infoFile)) {
			System.out.println("[warning] Trying to compile " + info.getFile()
					+ " and found it also within " + classUrl.getFile()
					+ " will not proceed. ");
			return;
		}
		byte[] bytes = AspectManager.instance().transform(loader, info.getClassName(), null, null, null);

		if (bytes == null) {
			System.out.println("[no comp needed] " + info.getFile());
			return;
		}

		FileOutputStream os = new FileOutputStream(infoFile);
		os.write(bytes);
		os.close();
		info.setCompiled(true);
		System.out.println("[compiled] " + info.getFile());
	}

	

	private void addDirectory(File dir) throws Exception {
		File[] directories = dir.listFiles(directoryFilter);
		File[] classFiles = dir.listFiles(classFileFilter);
		for (int i = 0; i < classFiles.length; i++) {
			addFile(classFiles[i]);
		}
		for (int i = 0; i < directories.length; i++) {
			addDirectory(directories[i]);
		}
	}

	private void addFile(File file) throws Exception {
		int index = file.getName().indexOf('$');
		if (index != -1) {
			String fileName = file.getName().substring(0, index) + ".class";
			File superClassFile = new File(fileName);
			// checking last modified date is not enough
			// because these values can differ in some miliseconds sometimes
			if (superClassFile.lastModified() > file.lastModified()
			// so, check this class has been recompiled and, hence,
			// is not Advised anymore
					&& !loadFile(superClassFile)) {
				file.delete();
				return;
			}
		}
		ClassFile cf = createClassFile(file);
		String className = cf.getName();
		String superClassName = cf.getSuperclass();
		CompilerClassInfo info = new CompilerClassInfo(file, className,
				superClassName);
		classesToCompile.put(className, info);
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

	private ClassFile createClassFile(final File file) throws Exception {
		DataInputStream is = new DataInputStream(new FileInputStream(file));
		ClassFile cf = new ClassFile(is);
		is.close();
		return cf;
	}

	private class CompilerClassInfo {
		File file;
		String className;
		String superClassName;
		boolean compiled;

		CompilerClassInfo(File file, String className, String superClassName) {
			this.file = file;
			this.className = className;
			this.superClassName = superClassName;
		}

		public File getFile() {
			return file;
		}

		public boolean isCompiled() {
			return compiled;
		}

		public void setCompiled(boolean compiled) {
			this.compiled = compiled;
		}

		public String getClassName() {
			return className;
		}

		public String getSuperClassName() {
			return superClassName;
		}
	}
}
