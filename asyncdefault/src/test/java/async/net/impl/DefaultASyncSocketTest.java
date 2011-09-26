package async.net.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import async.net.ASyncType;
import async.net.socket.SocketFactory;
import async.net.thread.ThreadHandler;

public class DefaultASyncSocketTest {

	@Mock
	private ThreadHandler handler;
	@Mock
	private SocketFactory socketFactory = mock(SocketFactory.class);
	@Mock
	private ExecutorService service = mock(ExecutorService.class);
	@Mock
	private ExecutorService service2 = mock(ExecutorService.class);

	@InjectMocks
	private DefaultASyncSocket socket = new DefaultASyncSocket(handler, null, null);

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(handler.getExecutorService(ASyncType.SOCKET_CONNECT)).thenReturn(service);
		when(handler.getExecutorService(ASyncType.SOCKET_LISTEN)).thenReturn(service);
		when(handler.getExecutorService(ASyncType.SOCKET_LISTEN_CONNECT)).thenReturn(service2);
	}

	@Test
	public void testConnectTo() throws IOException {
		socket.connectTo("host", 12345, null);
		verify(handler).getExecutorService(ASyncType.SOCKET_CONNECT);
		ArgumentCaptor<ConncetToRun> captor = ArgumentCaptor.forClass(ConncetToRun.class);
		verify(service).execute(captor.capture());
		ConncetToRun value = captor.getValue();
	}

	@Test
	public void testListenOn() throws IOException {
		socket.listenOn(12345, null);
		verify(handler).getExecutorService(ASyncType.SOCKET_LISTEN);
		verify(handler).getExecutorService(ASyncType.SOCKET_LISTEN_CONNECT);
		verify(service).execute(Mockito.any(ListenOnRun.class));
		verify(socketFactory).createServerSocket(12345, null);
	}

}
