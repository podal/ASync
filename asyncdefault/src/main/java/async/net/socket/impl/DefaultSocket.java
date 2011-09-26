package async.net.socket.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import async.net.socket.Socket;

public class DefaultSocket implements Socket {

	private java.net.Socket socket;

	public DefaultSocket(java.net.Socket socket) {
		this.socket = socket;
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

}
