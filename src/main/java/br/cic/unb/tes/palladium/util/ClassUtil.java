package br.cic.unb.tes.palladium.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javassist.bytecode.ClassFile;

public final class ClassUtil {

	public static ClassFile getClassFile(Class<?> clazz) throws IOException {
		String className = clazz.getSimpleName() + ".class";

		if (clazz.getPackage() != null) {
			String tPackageName = clazz.getPackage().getName();
			String tClassName = clazz.getSimpleName();
			tPackageName = tPackageName.replace('.', '/');
			className = tPackageName + '/' + tClassName + ".class";
		}

		InputStream input = null;
		BufferedInputStream bis = null;
		try {
			input = clazz.getClassLoader().getResourceAsStream(className);
			bis = new BufferedInputStream(input);
			ClassFile cf = new ClassFile(new DataInputStream(bis));
			return cf;
		} finally {
			if (input != null)
				input.close();

			if (bis != null)
				bis.close();
		}
	}

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
		}
		if (cl == null) {
			cl = ClassUtil.class.getClassLoader();
		}
		return cl;
	}

	public static Class<?>[] getClasses(ClassLoader classLoader,
			String packageName) throws IOException, ClassNotFoundException {

		if (classLoader != null) {

			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);

			List<File> dirs = new ArrayList<File>();

			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}

			ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

			for (File directory : dirs) {
				classes.addAll(findClasses(directory, packageName));
			}

			return classes.toArray(new Class[classes.size()]);
		}
		return null;
	}

	private static List<Class<?>> findClasses(File directory, String packageName)
			throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();

		if (!directory.exists()) {
			return classes;
		}

		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public static Class<?>[] getClasses(String packageName) throws IOException,
			ClassNotFoundException {
		return getClasses(ClassUtil.getDefaultClassLoader(), packageName);
	}

	public static Class<?>[] getClassesWith(String packageName,
			Class<? extends Annotation> annotation) throws IOException,
			ClassNotFoundException {

		if (annotation == null)
			throw new NullPointerException();

		final List<Class<?>> result = new ArrayList<Class<?>>();
		Class<?>[] classes = getClasses(getDefaultClassLoader(), packageName);

		if (classes != null) {
			for (Class<?> clazz : classes) {
				if (clazz.isAnnotationPresent(annotation))
					result.add(clazz);
			}
		}
		return result.toArray(new Class<?>[result.size()]);
	}
}
