package br.cic.unb.tes.palladium.standalone;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.jboss.aop.instrument.JoinPointGenerator;

import br.cic.unb.tes.palladium.util.ClassUtils;

public class PreparerClassFileTransformer implements ClassFileTransformer {

	private boolean isNonPrepareClassName(String classname) {
		return classname.startsWith("br.cic.unb.tes.palladium")
				|| (classname.startsWith("org.jboss.aop.")
						|| classname.startsWith("org.jboss")
						|| classname.startsWith("com.apple")
						|| classname.endsWith("$aop")
						|| classname.startsWith("javassist")
						|| classname.startsWith("org.jboss.util.")
						|| classname.startsWith("gnu.trove.")
						|| classname
								.startsWith("EDU.oswego.cs.dl.util.concurrent.")
						||
						// System classes
						classname.startsWith("org.apache.crimson")
						|| classname.startsWith("org.apache.xalan")
						|| classname.startsWith("org.apache.xml")
						|| classname.startsWith("org.apache.xpath")
						|| classname.startsWith("org.ietf.")
						|| classname.startsWith("org.omg.")
						|| classname.startsWith("org.w3c.")
						|| classname.startsWith("org.xml.sax.")
						|| classname.startsWith("sunw.")
						|| classname.startsWith("sun.")
						|| classname.startsWith("java.")
						|| classname.startsWith("javax.")
						|| classname.startsWith("com.sun.")
						|| classname.startsWith("$Proxy") || classname
						.contains('.' + JoinPointGenerator.JOINPOINT_CLASS_PREFIX));
	}

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		className = className.replace('/', '.');

		if (classBeingRedefined != null || isNonPrepareClassName(className)) {
			return null;
		}

		return aspectPrepare(className, loader, classBeingRedefined,
				protectionDomain, classfileBuffer);
	}

	private byte[] aspectPrepare(String className, ClassLoader loader,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) {
		byte[] bytes = null;
		try {
			String appClassPath = System.getProperty("app.class.path",
					ClassUtils.getAppPath());
			bytes = new Compiler().compileClass(className, appClassPath);
		} catch (Exception exception) {
			new RuntimeException(exception);
		}

		return bytes;
	}
}