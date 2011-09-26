package async.net.http.impl;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class DefaultHttpRequestTest {

	@Test
	public void testname() throws Exception {
		new DefaultHttpRequest(new ByteArrayInputStream("GET / HTTP/1.0\ntest\ntest\n\ntest".getBytes()));
	}
}