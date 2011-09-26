package async.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import async.net.callback.ExceptionCallback;
import async.net.callback.IOCallback;
import async.net.socket.Socket;

public class ListenOnRunAccept implements Runnable {
	private Socket socket;
	private ExceptionCallback<IOException> eCallback;
	private IOCallback ioCallback;

	public ListenOnRunAccept(Socket socket, IOCallback ioCallback, ExceptionCallback<IOException> eCallback) {
		this.socket = socket;
		this.eCallback = eCallback;
		this.ioCallback = ioCallback;
	}

	@Override
	public void run() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			ioCallback.call(inputStream, outputStream);
		} catch (IOException e) {
			if (eCallback != null) {
				eCallback.exception(e);
			}
		} catch (Throwable e2) {
		} finally {
			IOUtil.close(inputStream, eCallback);
			IOUtil.close(outputStream, eCallback);
			IOUtil.close(socket, eCallback);
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public IOCallback getIoCallback() {
		return ioCallback;
	}

	public ExceptionCallback<IOException> getExceptionCallback() {
		return eCallback;
	}

}
