package async.net;

import java.io.IOException;

import async.net.callback.IOCallback;

/**
 * Used to start ASync callback for sockets.
 * 
 * <pre>
 * <!--Code start[doc.JavaDocExample1.aSyncSocket] [8C071A6EF26233E47DDCE257BD838551]-->
 * 		ASyncSocket socket = new ASync().socket();
 * 		socket.connectTo("127.0.0.1", 12345, new IOCallback() {
 * 			public void call(InputStream in, OutputStream out) throws IOException {
 * 				// ...
 * 			}
 * 		});
 * <!--Code end-->
 * </pre>
 */
public interface ASyncSocket {

	public RemoteControl listenOn(int port, IOCallback ioCallback) throws IOException;

	public void connectTo(String host, int port, IOCallback ioCallback) throws IOException;

}
