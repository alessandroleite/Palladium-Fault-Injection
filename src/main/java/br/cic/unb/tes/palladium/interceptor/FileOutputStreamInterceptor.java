package br.cic.unb.tes.palladium.interceptor;

import java.io.FileOutputStream;

import net.vidageek.mirror.dsl.Mirror;

import org.jboss.aop.Bind;
import org.jboss.aop.InterceptorDef;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.joinpoint.CallerInvocation;
import org.jboss.aop.joinpoint.Invocation;

import br.cic.unb.tes.palladium.io.NullFileOutputStream;
import br.cic.unb.tes.palladium.manager.AspectManager;



@InterceptorDef(scope=Scope.PER_VM)
@Bind(pointcut="call(java.io.FileOutputStream->new(..))")
public class FileOutputStreamInterceptor implements Interceptor {
	
	@Override
	public String getName() {			
			return FileOutputStreamInterceptor.class.getCanonicalName();
	}
	
	@Override
	public Object invoke(Invocation invocation) throws Throwable {
		Object result = invocation.invokeNext();
		if (AspectManager.instance().getConfiguration().isInterceptorEnable(this.getClass()))
			if (result != null && FileOutputStream.class.isAssignableFrom(result.getClass())){
				if (invocation != null && CallerInvocation.class.isAssignableFrom(invocation.getClass())){
					Object[] args = ((CallerInvocation)invocation).getArguments();
					if (args != null && args.length > 0){
						return  new Mirror().on(NullFileOutputStream.class).invoke().constructor().withArgs(args);
					}				
				}
			}
		return result;
	}
}