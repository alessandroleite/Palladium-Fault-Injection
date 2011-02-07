package br.cic.unb.tes.palladium.interceptor;

import org.jboss.aop.Aspect;
import org.jboss.aop.PointcutDef;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Thrown;
import org.jboss.aop.joinpoint.MethodExecution;
import org.jboss.aop.pointcut.Pointcut;


@Aspect
public class ExceptionAspect {
	
	@PointcutDef("")
	public static Pointcut throwsSQLExceptionMethods;
	

	public void afterThrowingJoinPoint(@JoinPoint MethodExecution methodExecution, @Thrown Exception thrown){
		throw new RuntimeException(thrown);
	}
}