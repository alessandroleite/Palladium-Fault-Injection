package br.cic.unb.tes.palladium.interceptor;

import org.jboss.aop.Aspect;
import org.jboss.aop.Bind;
import org.jboss.aop.joinpoint.Invocation;

import br.cic.unb.tes.palladium.io.NullInputStream;
import br.cic.unb.tes.palladium.io.NullOutputStream;


@Aspect
public class IOAspect {
	
	@Bind(pointcut="execution(java.io.InputStream->new(..))")
	public Object newInputStreamAdvice(Invocation invocation){
		return new NullInputStream();
	}
	
	@Bind(pointcut="execution(java.io.OutputStream->new(..))")
	public Object newOutputStreamAdvice(Invocation invocation){
		return new NullOutputStream();
	}	
}