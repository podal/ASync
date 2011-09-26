package async.net.socket.impl;

import java.io.IOException;
import java.net.InetAddress;

import async.net.socket.ServerSocket;
import async.net.socket.Socket;
import async.net.socket.SocketFactory;

public class DefaultSocketFactory implements SocketFactory {

	@Override
	public ServerSocket createServerSocket(int port, InetAddress address) throws IOException {
		return new DefaultServerSocket(port,address);
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException {
		return new DefaultSocket(new java.net.Socket(host, port));
	}

}
