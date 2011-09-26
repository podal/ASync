package async.net.socket;

import java.io.IOException;
import java.net.InetAddress;

public interface SocketFactory {

	ServerSocket createServerSocket(int port, InetAddress address) throws IOException;

	Socket createSocket(String host, int port) throws IOException;

}
