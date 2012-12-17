package org.saabo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

public class FileUtil {

	public static byte[] readFileContent(final String path) throws Exception {
		URL url = FileUtil.class.getClassLoader().getResource(path);
		File file = new File(url.toURI());
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			byte[] buffer = new byte[1024];
			int readed = 0;
			while ((readed = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, readed);
			}
		} finally {
			fis.close();
		}

		return bos.toByteArray();
	}
}
