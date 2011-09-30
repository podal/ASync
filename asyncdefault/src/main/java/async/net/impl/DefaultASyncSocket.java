package async.net.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;

import async.net.ASyncSocket;
import async.net.ASyncType;
import async.net.RemoteControl;
import async.net.callback.ExceptionCallback;
import async.net.callback.IOCallback;
import async.net.socket.ServerSocket;
import async.net.socket.SocketFactory;
import async.net.socket.impl.DefaultSocketFactory;
import async.net.thread.ThreadHandler;

public class DefaultASyncSocket implements ASyncSocket {

	private ThreadHandler handler;

	private SocketFactory socketFactory = new DefaultSocketFactory();

	private ExceptionCallback<IOException> exceptionCallback;

	private InetAddress address;

	public DefaultASyncSocket(ThreadHandler handler, InetAddress address, ExceptionCallback<IOException> exceptionCallback) {
		this.handler = handler;
		this.exceptionCallback = exceptionCallback;
		this.address = address;
	}

	@Override
	public RemoteControl listenOn(int port, IOCallback ioCallback)
			throws IOException {
		final ExecutorService executorService = handler.getExecutorService(ASyncType.SOCKET_LISTEN);
		final ExecutorService executorServiceConnect = handler.getExecutorService(ASyncType.SOCKET_LISTEN_CONNECT);
		final ServerSocket serverSocket = socketFactory.createServerSocket(port,address);
		ListenOnRun listenOnRun = new ListenOnRun(executorServiceConnect, serverSocket, ioCallback, exceptionCallback);
		executorService.execute(listenOnRun);
		return listenOnRun;
	}

	@Override
	public void connectTo(String host, int port, IOCallback callback)
			throws IOException {
		final ExecutorService executorService = handler.getExecutorService(ASyncType.SOCKET_CONNECT);
		executorService.execute(new ConncetToRun(socketFactory.createSocket(host, port), callback, exceptionCallback));
	}

}
