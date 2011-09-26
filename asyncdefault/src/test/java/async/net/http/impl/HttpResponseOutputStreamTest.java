package async.net.http.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class HttpResponseOutputStreamTest {

	@Test
	public void testFlush() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DefaulHttpResponse response = Mockito.mock(DefaulHttpResponse.class);
		Mockito.when(response.getHead()).thenReturn(new byte[0]);
		HttpResponseOutputStream out2 = new HttpResponseOutputStream(response, out);
		out2.write("test".getBytes());
		out2.write('1');
		Assert.assertEquals(0, out.size());
		out2.flush();
		Assert.assertEquals(5, out.size());
		out2.write("test".getBytes());
		out2.write('2');
		Assert.assertEquals("test1test2", out.toString());
	}

}
