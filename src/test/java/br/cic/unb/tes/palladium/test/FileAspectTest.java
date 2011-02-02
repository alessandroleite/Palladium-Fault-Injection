package br.cic.unb.tes.palladium.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class FileAspectTest {

	@Test
	public void deve_criar_arquivo_temporario_somente_leitura() {
		try {
			File file = File.createTempFile(FileAspectTest.class.getName(),
					Long.toString(System.currentTimeMillis()));
			Assert.assertNotSame(true, file.canWrite());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
