package async.net.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import async.net.callback.IOCallback;
import async.net.socket.Socket;

public class ConncetToRunTest {

	@Test
	public void testRun() throws IOException {
		IOCallback callback = mock(IOCallback.class);
		Socket socket = mock(Socket.class);
		InputStream in = mock(InputStream.class);
		OutputStream out = mock(OutputStream.class);

		when(socket.getInputStream()).thenReturn(in);
		when(socket.getOutputStream()).thenReturn(out);

		ConncetToRun run = new ConncetToRun(socket, callback, null);
		run.run();

		verify(callback).call(in, out);
		verify(in).close();
		verify(out).close();
		verify(socket).close();
	}

}
