package br.cic.unb.tes.palladium.io;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class can be used as an {@link InputStream} to inject a fault where the
 * result of {@link InputStream#available()} is always gather than zero, but,
 * the result of {@link InputStream#read()} is always -1.
 */
public class NullFileOutputStream extends java.io.FileOutputStream {

	private static final int DEFAULT_BYTE_AVALIABLE = 100;
	
	

	public NullFileOutputStream(String s) throws FileNotFoundException{		
		super(s);
	}
	
	public NullFileOutputStream(String s, Boolean b) throws FileNotFoundException{		
		super(s,b);
	}
	
	public NullFileOutputStream(File f) throws FileNotFoundException {
		super(f);
	}
	
	public NullFileOutputStream(File f, Boolean b) throws FileNotFoundException {
		super(f,b);
	}
	
	public NullFileOutputStream(FileDescriptor fd) {
		super(fd);
	}

	public int read() throws IOException {
		return -1;
	}

	public int available() throws IOException {
		return DEFAULT_BYTE_AVALIABLE;
	}
}