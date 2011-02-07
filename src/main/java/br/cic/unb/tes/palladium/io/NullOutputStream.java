package br.cic.unb.tes.palladium.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class can be used as {@link OutputStream} to inject a fault that the
 * bytes are never in the output.
 */
public class NullOutputStream extends OutputStream {

	public void write(int b) throws IOException {
		// do nothing
	}
}