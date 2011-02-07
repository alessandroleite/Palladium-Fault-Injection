package br.cic.unb.tes.palladium.manager;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.AspectFactory;
import org.jboss.aop.advice.GenericAspectFactory;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.pointcut.ast.ParseException;

public class Binding {

	public void bindingAdvice(Class<?>[] classes, Class<? super Interceptor>[] interceptors,
			String[] pointCutExpressions) throws ParseException {
		assert !(classes.length == interceptors.length && interceptors.length == pointCutExpressions.length);

		for (int i = 0; i < classes.length; i++) {
			AdviceBinding binding = new AdviceBinding(classes[i].getSimpleName(), pointCutExpressions[i], null);
			binding.addInterceptor(interceptors[i]);
			AspectManager.instance().addBinding(binding);
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