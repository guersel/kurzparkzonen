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
	
	/** The target http address. **/
	protected URL url;
	protected HttpURLConnection connection;
	
	public Http(final String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	/**
	 * Make a GET request.
	 * @return The response of the GET request
	 * @throws IOException if an error occurs
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

	/**
	 * Put data from an <code>InputStream</code> to a <code>byte[]</code>.
	 * @param is The <code>InputStream</code> as source
	 * @return Contains data from the <code>InputStream</code>
	 * @throws IOException if an error occurs
	 */
	protected byte[] readStream(final InputStream is) throws IOException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int result = 0;
		
		while((result = is.read(buffer)) != -1) {
			bao.write(buffer, 0, result);
		}
		
		return bao.toByteArray();
	}
	
}
