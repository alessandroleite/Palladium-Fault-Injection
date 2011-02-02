package br.cic.unb.tes.palladium.interceptor;

import java.io.File;

import org.jboss.aop.Aspect;
import org.jboss.aop.Bind;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.joinpoint.Invocation;

/**
 * This aspect intercept execution of method {@link File#createNewFile()} of
 * {@link File} simulating a fault where the result of invocation is
 * <code>true</code>, but the file is erase of disk.
 */
@Aspect(scope=Scope.PER_VM)
public class FileAspect {

	@Bind(pointcut = "execution(* java.io.File->createNewFile(..))")
	public Object createNewFile(Invocation invocation) throws Throwable {
		Object ret = invocation.invokeNext();
		File target = (File) invocation.getTargetObject();
		target.delete();
		return ret;
	}

	/**
	 * Advice method to intercept execution of
	 * {@link File#createTempFile(String, String)} to set the created file to be
	 * only read.
	 * 
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	@Bind(pointcut = "execution(* java.io.File->createTempFile(..))")
	public Object createTempFileOnlyRead(Invocation invocation)
			throws Throwable {
		Object ret = invocation.invokeNext();
		File target = (File) invocation.getTargetObject();
		target.setReadOnly();
		return ret;
	}

	/**
	 * Advice method to intercept execution of
	 * {@link File#createTempFile(String, String)} to delete the created file.
	 * 
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	@Bind(pointcut = "execution(* java.io.File->createTempFile(..))")
	public Object createAndDeleteTempFile(Invocation invocation)
			throws Throwable {
		Object ret = invocation.invokeNext();
		File target = (File) invocation.getTargetObject();
		target.delete();
		return ret;
	}
}