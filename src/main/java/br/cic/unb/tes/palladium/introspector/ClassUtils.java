package br.cic.unb.tes.palladium.introspector;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

@SuppressWarnings("rawtypes")
public class ClassUtils {
	
	private static final String CLASS_SUFFIX = ".class";	
	
	private static final String REGEX_DOT = "\\.";
	
	private static final String FILE_SEPARATOR = File.separatorChar+"";
	
	
	
	public static class ClassFinderException extends RuntimeException {		
				
		private static final long serialVersionUID = 5309532665686994276L;
		
		public ClassFinderException(String msg, Throwable cause) {
			super(msg,cause);
		}
	}
	
	public static boolean isApplicationJar(){
		try {
			File f = new File(ClassUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			return f.isFile() && f.getAbsolutePath().endsWith(".jar");
		} catch (URISyntaxException e) {
			throw new ClassFinderException(null,e);
		}
	}
	
	
	/**
	 * Returns the current application root path.
	 * @return
	 */
	public static String getAppPath(){
		try {
			File f = new File(ClassUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			return f.getAbsolutePath();			
		} catch (URISyntaxException e) {
			throw new ClassFinderException(null,e);
		}
	}
	
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws URISyntaxException 
     */    
	public static List<Class> getApplicationClasses(String packageName)  {
		try {
	        File f = new File(ClassUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI());
	        List<Class> classes = null;
	        if (f.isDirectory()){
	        	classes = findClassesInsideDir(f, packageName);
	        } else if (f.isFile()){
	        	classes = findClassesInsideJar(f, packageName);
	        } else {
	        	throw new ClassFinderException("Cannot find classes", null);
	        }              
	        return classes;
		} catch (ClassNotFoundException e){
			throw  new ClassFinderException(e.getMessage(), e);
		} catch (ZipException e) {
			throw new ClassFinderException(e.getMessage(), e);
		} catch (URISyntaxException e) {
			throw new ClassFinderException(e.getMessage(), e);
		} catch (IOException e) {
			throw new ClassFinderException(e.getMessage(), e);
		}		
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClassesInsideDir(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }   
        
        List<File> files = new ArrayList<File>(Arrays.asList(directory.listFiles()));
        while(!files.isEmpty()){
			File file = files.get(0);
			files.remove(file);
		    if (file.isDirectory()){		    
		    	files.addAll(Arrays.asList(file.listFiles()));
		    } else if (file.isFile()){
		    	Class clazz = fileNameToClass(file.getAbsolutePath(), packageName);
		        if (clazz != null){
		          classes.add(clazz);     
		        }
		    }		   			
		}
        return classes;
    }
    
    public static List<Class> findClassesInsideJar(File jar, String packageName) throws URISyntaxException, ZipException, IOException, ClassNotFoundException{	  	  
		  ZipFile zf=new ZipFile(jar.getAbsoluteFile());
	      Enumeration e = zf.entries();
	      
	      List<Class> classes = new ArrayList<Class>();
	      while (e.hasMoreElements()) {	    	  
	          ZipEntry ze=(ZipEntry)e.nextElement();	          
	          Class clazz = null;
	          try{
	        	  clazz = fileNameToClass(ze.getName(), packageName);
	          } catch (ClassNotFoundException exp){
	        	  //do nothing
	          }  catch (NoClassDefFoundError exp){
	        	//do nothing
	          }
	          if (clazz != null){
	        	  classes.add(clazz);     
	          }
	      }
	      zf.close();	
	      return classes;
    }
    
    
    private static Class fileNameToClass(String fileName, String packageName) throws ClassNotFoundException{
    	String path = packageName.replaceAll(REGEX_DOT,FILE_SEPARATOR);
    	if (fileName.contains(path) && fileName.endsWith(CLASS_SUFFIX)){	    		    	
	  	  	return Class.forName(fileName.substring(fileName.indexOf(path),fileName.length() - CLASS_SUFFIX.length()).replaceAll(FILE_SEPARATOR,REGEX_DOT));
    	} else {
    		return null;
    	}
    }

}
