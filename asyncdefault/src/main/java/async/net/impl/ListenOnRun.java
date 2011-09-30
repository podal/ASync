package async.net.impl;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import async.net.RemoteControl;
import async.net.callback.ExceptionCallback;
import async.net.callback.IOCallback;
import async.net.socket.ServerSocket;
import async.net.socket.Socket;

public class ListenOnRun implements Runnable, RemoteControl {

	private ServerSocket serverSocket;
	private IOCallback ioCallback;
	private ExecutorService executorService;
	private ExceptionCallback<IOException> eCallback;
	private AtomicBoolean active = new AtomicBoolean(true);

	public ListenOnRun(ExecutorService executorService, ServerSocket serverSocket, IOCallback ioCallback,
			ExceptionCallback<IOException> eCallback) {
		this.executorService = executorService;
		this.serverSocket = serverSocket;
		this.ioCallback = ioCallback;
		this.eCallback = eCallback;
	}

	@Override
	public void run() {
		try {
			while (active.get()) {
				final Socket socket = serverSocket.accept();
				ListenOnRunAccept accept = new ListenOnRunAccept(socket, ioCallback, eCallback);
				executorService.execute(accept);
			}
		} catch (IOException e) {
			if (eCallback != null) {
				eCallback.exception(e);
			}
		}
		active.set(false);
	}

	@Override
	public boolean isActive() {
		return active.get();
	}

	@Override
	public void stop() {
		IOUtil.close(serverSocket, eCallback);
		active.set(false);
	}

}
