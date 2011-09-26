package async.net.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import async.net.callback.ExceptionCallback;
import async.net.callback.IOCallback;
import async.net.socket.ServerSocket;
import async.net.socket.Socket;

public class ListenOnRunTest {

	@SuppressWarnings("unchecked")
	private ExceptionCallback<IOException> eCallback = mock(ExceptionCallback.class);

	private ExecutorService executorService = mock(ExecutorService.class);

	private ServerSocket serverSocket = mock(ServerSocket.class);

	private IOCallback ioCallback = mock(IOCallback.class);

	private ListenOnRun listenOnRun = new ListenOnRun(executorService, serverSocket, ioCallback, eCallback);

	@Test
	public void testRun() throws IOException {
		final Socket socket = mock(Socket.class);
		when(serverSocket.accept()).thenAnswer(new Answer<Socket>() {

			@Override
			public Socket answer(InvocationOnMock invocation) throws Throwable {
				listenOnRun.stop();
				return socket;
			}
		});
		listenOnRun.run();
		verify(serverSocket).accept();
		ArgumentCaptor<ListenOnRunAccept> captor = ArgumentCaptor.forClass(ListenOnRunAccept.class);
		verify(executorService).execute(captor.capture());
		ListenOnRunAccept runAccept = captor.getValue();
		assertEquals(socket, runAccept.getSocket());
		assertEquals(ioCallback, runAccept.getIoCallback());
		assertEquals(eCallback, runAccept.getExceptionCallback());
	}

	@Test
	public void testRun_IOException() throws IOException {
		IOException ioException = new IOException();
		when(serverSocket.accept()).thenThrow(ioException);
		listenOnRun.run();
		verify(eCallback).exception(ioException);
	}

	@Test
	public void testIsActive() {
		assertEquals(true, listenOnRun.isActive());
		listenOnRun.stop();
		assertEquals(false, listenOnRun.isActive());
	}

	@Test
	public void testStop() throws IOException {
		listenOnRun.stop();
		verify(serverSocket).close();
		assertEquals(false, listenOnRun.isActive());
	}

}
