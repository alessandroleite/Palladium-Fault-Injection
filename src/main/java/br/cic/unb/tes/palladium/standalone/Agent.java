package br.cic.unb.tes.palladium.standalone;

import java.lang.instrument.Instrumentation;

import br.cic.unb.tes.palladium.manager.AspectManager;
import br.cic.unb.tes.palladium.util.ClassUtils;

/**
 * This agent prepare all class to be adviced by Jboss AOP.
 */
public class Agent {

	private static Instrumentation instrumentation;

	public static Instrumentation getInstrumentation() {
		return instrumentation;
	}

	public static void premain(String agentArgs, Instrumentation inst) {
		instrumentation = inst;

		if (agentArgs != null
				&& (agentArgs.indexOf(".jar") != -1 || (agentArgs.indexOf("/") != -1))) {
			System.setProperty("app.class.path", agentArgs);

			// to help the launcher;
			System.setProperty("jboss.aop.class.path", ClassUtils.getAppPath());
			System.setProperty("jboss.aop.path", agentArgs);
		}
		
		AspectManager.instance();
		org.jboss.aop.standalone.Agent.premain("-hotSwap", inst);
	}
}