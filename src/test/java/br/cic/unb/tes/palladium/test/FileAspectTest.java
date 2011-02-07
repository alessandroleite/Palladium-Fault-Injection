package br.cic.unb.tes.palladium.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class FileAspectTest {

	@Test
	public void nao_deve_existir_arquivo_temporario_criado() {
		try {
			File file = File.createTempFile(FileAspectTest.class.getName(),
					Long.toString(System.currentTimeMillis()));
			Assert.assertNotSame(true, file.exists());
			
			new FileOutputStream(file);
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
