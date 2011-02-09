package br.cic.unb.tes.palladium.io;

import java.io.EOFException;
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
public class NullFileInputStream extends java.io.FileInputStream {

	private static final int DEFAULT_BYTE_AVALIABLE = 100;

	public NullFileInputStream(String s) throws FileNotFoundException{
		super(s);
	}
	
	public NullFileInputStream(File f) throws FileNotFoundException {
		super(f);
	}
	
	public NullFileInputStream(FileDescriptor fd) {
		super(fd);
	}

	public int read() throws IOException {
		return -1;
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		throw new EOFException();
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {	
		throw new EOFException();
	}

	public int available() throws IOException {
		return DEFAULT_BYTE_AVALIABLE;
	}
}