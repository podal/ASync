package async.net.impl;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Closeable;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import async.net.callback.ExceptionCallback;

public class IOUtilTest {
	@SuppressWarnings("unchecked")
	private ExceptionCallback<IOException> eCallback = mock(ExceptionCallback.class);
	private Closeable closeable = mock(Closeable.class);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testClose() throws IOException {
		IOUtil.close(closeable, eCallback);
		verify(closeable).close();
	}

	@Test
	public void testClose_IOException() throws IOException {
		IOException e = new IOException();
		doThrow(e).when(closeable).close();
		IOUtil.close(closeable, eCallback);
		verify(closeable).close();
		verify(eCallback).exception(e);
	}

	@Test
	public void testClose_Null() throws IOException {
		IOUtil.close(null, eCallback);
	}
}
