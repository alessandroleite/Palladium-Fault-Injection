package br.cic.unb.tes.palladium.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.aop.Bind;
import org.jboss.aop.InterceptorDef;
import org.jboss.aop.advice.Interceptor;

@SuppressWarnings({"unchecked","rawtypes"})
public class LoadConfiguration {
	
	private final static Logger LOGGER = Logger.getLogger(LoadConfiguration.class .getName());	
			
	public void applyConfiguration(Properties properties){				
		Configuration config = parseConfiguration(properties);
		Binding bindings = new Binding();		
		bindings.clearInterceptorsBindings();
		bindings.bindInterceptors(config.getInterceptorsArray(), config.getPointcutsArray());	
	}
	
	private Configuration parseConfiguration(Properties properties){
		Configuration config = new Configuration();
		for(Object className : properties.keySet()){
			try {
				Class clazz = Class.forName((String)className);
				if (Interceptor.class.isAssignableFrom(clazz)){
					if (clazz.isAnnotationPresent(InterceptorDef.class)){
						String pointcut = properties.getProperty((String) className);
						if (pointcut == null || pointcut.isEmpty()){
							if (clazz.isAnnotationPresent(Bind.class)){
								pointcut = ((Bind)clazz.getAnnotation(Bind.class)).pointcut();								
							} else {
								continue;
							}
						}
						config.add(clazz, pointcut);						
					} else {
						LOGGER.log(Level.INFO,"Class "+className+" does not have "+InterceptorDef.class.getCanonicalName()+" annotation");	
					}
				} else {
					LOGGER.log(Level.INFO,"Class "+className+" does not implements "+Interceptor.class.getCanonicalName());
				}
			} catch (ClassNotFoundException e) {
				LOGGER.log(Level.INFO,"Class "+className+" not found");
			}
		}
		return config;
	}
	
	
	private class Configuration{
		private List<Class>  classes = new ArrayList<Class>();
		private List<String> pointcuts = new ArrayList<String>();
		
		public void add(Class clazz, String pointcut){
			if (clazz != null && pointcut != null){
				classes.add(clazz);
				pointcuts.add(pointcut);
			}
		}
			
		public Class[] getInterceptorsArray(){
			return classes.toArray(new Class[0]);
		}
		
		public String[] getPointcutsArray(){
			return pointcuts.toArray(new String[0]);
		}
		
	}

}
