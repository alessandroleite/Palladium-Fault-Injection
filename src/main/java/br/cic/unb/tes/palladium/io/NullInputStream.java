package br.cic.unb.tes.palladium.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class can be used as an {@link InputStream} to inject a fault where the
 * result of {@link InputStream#available()} is always gather than zero, but,
 * the result of {@link InputStream#read()} is always -1.
 */
public class NullInputStream extends java.io.InputStream {

	private static final int DEFAULT_BYTE_AVALIABLE = 100;

	public NullInputStream() {
	}

	public int read() throws IOException {
		return -1;
	}

	public int available() throws IOException {
		return DEFAULT_BYTE_AVALIABLE;
	}
}