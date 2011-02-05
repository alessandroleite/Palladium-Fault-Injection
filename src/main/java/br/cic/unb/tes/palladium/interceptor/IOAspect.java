package br.cic.unb.tes.palladium.interceptor;

import org.jboss.aop.Aspect;
import org.jboss.aop.Bind;
import org.jboss.aop.joinpoint.Invocation;

import br.cic.unb.tes.palladium.io.NullFileInputStream;

@Aspect
public class IOAspect {
	
	@Bind(pointcut="call(public java.io.FileInputStream->new(..))")
	public Object newFileInputStreamAdvice(Invocation invocation) throws Throwable{		
		invocation.invokeNext();		
		return new NullFileInputStream("");
	}
	
	@Bind(pointcut="call(public java.io.OutputStream->new(..))")
	public Object newOutputStreamAdvice(Invocation invocation) throws Throwable{
		invocation.invokeNext();
		return null;
	}	
	
	@Bind(pointcut="call(public java.io.FileOutputStream->new(..))")
	public Object newFileOutputStreamAdvice(Invocation invocation) throws Throwable{
		invocation.invokeNext();
		return null;
	}	
}