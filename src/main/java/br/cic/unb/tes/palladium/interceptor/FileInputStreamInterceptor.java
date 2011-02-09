package br.cic.unb.tes.palladium.interceptor;

import java.io.FileInputStream;

import net.vidageek.mirror.dsl.Mirror;

import org.jboss.aop.Bind;
import org.jboss.aop.InterceptorDef;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.joinpoint.CallerInvocation;
import org.jboss.aop.joinpoint.Invocation;

import br.cic.unb.tes.palladium.io.NullFileInputStream;
import br.cic.unb.tes.palladium.manager.AspectManager;



@InterceptorDef(scope=Scope.PER_VM)
@Bind(pointcut="call(java.io.FileInputStream->new(..))")
public class FileInputStreamInterceptor implements Interceptor {
	
	@Override
	public String getName() {			
			return FileInputStreamInterceptor.class.getName();
	}
	
	@Override
	public Object invoke(Invocation invocation) throws Throwable {
		Object result = invocation.invokeNext();
		if (AspectManager.instance().getConfiguration().isInterceptorEnable(this.getClass()))
			if (result != null && FileInputStream.class.isAssignableFrom(result.getClass())){
				if (invocation != null && CallerInvocation.class.isAssignableFrom(invocation.getClass())){
					Object[] args = ((CallerInvocation)invocation).getArguments();
					if (args != null && args.length > 0){
						return  new Mirror().on(NullFileInputStream.class).invoke().constructor().withArgs(args);					
					}				
				}
			}	
		return result;
	}
}