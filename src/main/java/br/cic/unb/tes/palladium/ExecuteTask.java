package br.cic.unb.tes.palladium;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import br.cic.unb.tes.palladium.introspector.ClassUtils;

public class ExecuteTask {
	
	public void aspectize(String appPath, String appClassPath, List<String> selectedAspects) throws IOException{		
		File buildFile = prepareBuildFile();
		Project p = new Project();
		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
		p.addBuildListener(consoleLogger);
		p.setUserProperty("ant.file", buildFile.getAbsolutePath());
		System.out.println(appClassPath);
		p.setUserProperty("classes.dir", appPath);
		p.setUserProperty("classes.dir.lib", appPath+File.pathSeparator+appClassPath);		
		if (ClassUtils.isApplicationJar()){
			p.setUserProperty("tool.classpath",ClassUtils.getAppPath());			
		} else {
			p.setUserProperty("tool.classpath", System.getProperty("java.class.path") );
		}
		p.setUserProperty("aop.classpath", prepareSelectecAspectsClassPath(selectedAspects));
		p.setProperty("basedir",getCurrentExecutionPath());
		p.init();		
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		p.addReference("ant.projectHelper", helper);
		helper.parse(p, buildFile);
		p.executeTarget(p.getDefaultTarget());
	}
	
	private String getCurrentExecutionPath(){
		return new File(".").getAbsolutePath();
	}
	
	private ClassLoader getDefaultClassLoader(){
		ClassLoader cl = null;
		cl = Thread.currentThread().getContextClassLoader();
		if (cl == null){
			cl = ExecuteTask.class.getClassLoader();
		}
		return cl;
	}
	
	private File prepareBuildFile() throws IOException{					
		File buildFile = File.createTempFile("build"+Calendar.getInstance().getTimeInMillis(),Math.random()+".xml");		
		buildFile.deleteOnExit();
		writeResource("anttasktemplate.xml", buildFile);		
		return buildFile;
	}
	
	private String prepareSelectecAspectsClassPath(List<String> selectedAspects){
		try {				
			File tempDir = File.createTempFile("selectedAspects"+UUID.randomUUID().toString(),"");
			if(tempDir.exists()){
				tempDir.delete();
			}
			tempDir.mkdir();
			tempDir.deleteOnExit();
			for(String aspect : selectedAspects){
				String fileName = aspect.substring(aspect.lastIndexOf(".")+1,aspect.length());
				String dirName = aspect.replaceAll("\\.",File.separator+"").substring(0,aspect.length()-fileName.length());
				File packageDir = new File(tempDir,dirName);				
				if (packageDir.exists()){
					packageDir.delete();
				}
				packageDir.mkdirs();			
				packageDir.deleteOnExit();				
				File aux = new File(packageDir,fileName+".class");
				writeResource(dirName+fileName+".class", aux);
				aux.deleteOnExit();
			}
			return tempDir.getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}
	
	private void writeResource(String resourceName, File f) throws IOException{
		InputStream ins = getDefaultClassLoader().getResourceAsStream(resourceName);			
		if (ins != null){
			FileOutputStream fos = new FileOutputStream(f);
			byte[] buff = new byte[20];
			int size = 0;
			while ((size = ins.read(buff)) != -1){
				fos.write(buff,0,size);
			}
			fos.flush();
			fos.close();
			ins.close();	
		}
	}
}
