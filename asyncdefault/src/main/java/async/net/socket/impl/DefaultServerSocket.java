package async.net.socket.impl;

import java.io.IOException;
import java.net.InetAddress;

import async.net.socket.ServerSocket;
import async.net.socket.Socket;

public class DefaultServerSocket implements ServerSocket {
	private java.net.ServerSocket serverSocket;

	public DefaultServerSocket(int port, InetAddress address) throws IOException {
		if (address == null) {
			serverSocket = new java.net.ServerSocket(port);
		} else {
			serverSocket = new java.net.ServerSocket(port, 0, address);
		}
	}

	@Override
	public void close() throws IOException {
		serverSocket.close();
	}

	@Override
	public Socket accept() throws IOException {
		return new DefaultSocket(serverSocket.accept());
	}

}
