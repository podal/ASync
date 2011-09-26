package async.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import async.net.callback.ExceptionCallback;
import async.net.callback.IOCallback;
import async.net.socket.Socket;

public class ConncetToRun implements Runnable {
	private IOCallback callback;
	private ExceptionCallback<IOException> eCallback;
	private Socket socket;

	public ConncetToRun(Socket socket, IOCallback callback, ExceptionCallback<IOException> eCallback) {
		this.socket = socket;
		this.callback = callback;
		this.eCallback = eCallback;
	}

	@Override
	public void run() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			callback.call(inputStream, outputStream);
		} catch (IOException e) {
			if (eCallback != null) {
				eCallback.exception(e);
			}
		} finally {
			IOUtil.close(inputStream, eCallback);
			IOUtil.close(outputStream, eCallback);
			IOUtil.close(socket, eCallback);
		}
	}

}
