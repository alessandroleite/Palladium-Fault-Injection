package br.cic.unb.tes.palladium.interceptor;

import java.io.FileInputStream;
import java.io.InputStream;

import org.jboss.aop.Aspect;
import org.jboss.aop.Bind;
import org.jboss.aop.PointcutDef;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Target;
import org.jboss.aop.joinpoint.ConstructorExecution;
import org.jboss.aop.pointcut.Pointcut;

import br.cic.unb.tes.palladium.io.NullInputStream;

@Aspect
public class CallAspect{
	
	@PointcutDef("execution(public java.io.FileInputStream->new(..))")
	public static Pointcut constructorInputStreamMethod;
	
	@Bind(pointcut="execution(public java.io.FileInputStream->new(..))")
	public InputStream around(@JoinPoint ConstructorExecution constructor, @Target FileInputStream is) {
		return new NullInputStream();
	}
}
