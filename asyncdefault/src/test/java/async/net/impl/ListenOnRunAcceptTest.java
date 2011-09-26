package async.net.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import async.net.callback.ExceptionCallback;
import async.net.callback.IOCallback;
import async.net.socket.Socket;

public class ListenOnRunAcceptTest {

	private Socket socket = mock(Socket.class);
	private IOCallback ioCallback = mock(IOCallback.class);
	@SuppressWarnings("unchecked")
	private ExceptionCallback<IOException> eCallback = mock(ExceptionCallback.class);
	private ListenOnRunAccept accept = new ListenOnRunAccept(socket, ioCallback, eCallback);
	private InputStream in;
	private OutputStream out;

	@Before
	public void setUp() throws IOException {
		in = mock(InputStream.class);
		out = mock(OutputStream.class);
	}

	@Test
	public void testRun() throws IOException {
		when(socket.getInputStream()).thenReturn(in);
		when(socket.getOutputStream()).thenReturn(out);
		accept.run();

		verify(ioCallback).call(in, out);
		verify(in).close();
		verify(out).close();
		verify(socket).close();
	}

	@Test
	public void testRun_RuntimeException() throws IOException {
		when(socket.getInputStream()).thenThrow(new RuntimeException());
		accept.run();
		verify(socket).close();
	}

	@Test
	public void testRun_IOException() throws IOException {
		IOException ioException = new IOException();
		when(socket.getInputStream()).thenThrow(ioException);
		accept.run();
		verify(eCallback).exception(ioException);
		verify(socket).close();
	}
}
