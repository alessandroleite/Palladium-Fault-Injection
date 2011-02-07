package br.cic.unb.tes.palladium.manager;

import org.jboss.aop.Aspect;
import org.jboss.aop.AspectManager;
import org.jboss.aop.Deployment;

import br.cic.unb.tes.palladium.util.ClassUtil;

public class AspectAnnotationLoader {

	private static final String JBOSS_AOP_CLASS_PATH = "jboss.aop.class.path";

	private org.jboss.aop.AspectAnnotationLoader loader;

	public AspectAnnotationLoader() {
		loader = new org.jboss.aop.AspectAnnotationLoader(AspectManager.instance());
	}

	public void deployClassFile(Class<?>... classes) throws Exception {
		if (classes != null) {
			for (Class<?> clazz : classes) {
				this.loader.deployClassFile(ClassUtil.getClassFile(clazz));
			}
		}
	}

	public void deployAllClassesFromPackage(String packageName)
			throws Exception {
		this.deployClassFile(ClassUtil.getClassesWith(packageName, Aspect.class));
	}

	/**
	 * Tell JBoss AOP which JARS or directories that may have annotated
	 * {@link Aspect}s. You can specify multiple jars or directories separated
	 * by ':' (Unix) or ';' (Windows), i.e.:
	 * /Users/lib/myAspects.jar;/Users/classes.
	 * 
	 * @param value
	 */
	public void deployThroughClassAnnotationsInJarOrClassDirectory(String value) {
		System.setProperty(JBOSS_AOP_CLASS_PATH, value);
		Deployment.deployThroughClassAnnotations();
	}
}
