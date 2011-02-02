package br.cic.unb.tes.palladium.interceptor;

import org.jboss.aop.Aspect;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Thrown;
import org.jboss.aop.joinpoint.MethodExecution;


@Aspect(scope=Scope.PER_VM)
public class ExceptionInterceptor {
	
	public void afterThrowingJoinPoint(@JoinPoint MethodExecution methodExecution, @Thrown Exception thrown){
		throw new RuntimeException(thrown);
	}
}