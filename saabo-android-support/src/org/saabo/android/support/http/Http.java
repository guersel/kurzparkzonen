package org.saabo.android.support.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class allows to make easily http method calls (GET, POST and so on).
 * @author guersel
 *
 */
public class Http {	
	
	protected URL url;
	protected HttpURLConnection connection;
	
	/**
	 * Constructor.
	 * @param url the address
	 * @throws MalformedURLException
	 */
	public Http(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	/**
	 * Make a GET request.
	 * @return the response of the GET request
	 * @throws IOException
	 */
	public byte[] doGet() throws IOException {
		connection = (HttpURLConnection) url.openConnection();
		
		try {
			InputStream is = new BufferedInputStream(connection.getInputStream());
			return readStream(is);
		} finally {
			connection.disconnect();
		}
	}

	// Generel method to read from an InputStream
	protected byte[] readStream(InputStream is) throws IOException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int result = 0;
		
		while((result = is.read(buffer)) != -1) {
			bao.write(buffer, 0, result);
		}
		
		return bao.toByteArray();
	}
	
}
