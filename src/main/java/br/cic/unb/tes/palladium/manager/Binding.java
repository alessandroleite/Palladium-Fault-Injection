package br.cic.unb.tes.palladium.manager;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.AspectFactory;
import org.jboss.aop.advice.GenericAspectFactory;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.pointcut.ast.ParseException;

public class Binding {

	private final static Logger LOGGER = Logger.getLogger(Binding.class .getName());
	
	public void bindInterceptors(Class<? super Interceptor>[] interceptors, String[] pointCutExpressions) {
		assert !(interceptors.length == pointCutExpressions.length);

		for (int i = 0; i < interceptors.length; i++) {
			
			AdviceBinding binding;
			try {
				binding = new AdviceBinding(interceptors[i].getName(),pointCutExpressions[i], null);
				binding.addInterceptor(interceptors[i]);
				AspectManager.instance().addBinding(binding);
			} catch (ParseException e) {
				LOGGER.log(Level.WARNING,"Error parsing pointcut ["+pointCutExpressions[i]+"], pointcut ignored");
			}
		}
	}
	
	public void clearInterceptorsBindings(){		
		Map<String,AdviceBinding> bindinds = AspectManager.instance().getBindings();
		for (String name :  bindinds.keySet()) {
			AspectManager.instance().removeBinding(name);
		}
	}
	

	public void addAspect(String[] aspectClasses) {
		if (aspectClasses == null) return;		
		for (int i = 0; i < aspectClasses.length; i++) {
			AspectFactory factory = new GenericAspectFactory(aspectClasses[i], null);
			AspectDefinition def = new AspectDefinition(aspectClasses[i], Scope.PER_VM, factory);
			AspectManager.instance().addAspectDefinition(def);
		}
	}
}