package async.example.socket;

import java.io.IOException;

import async.net.ASync;
import async.net.callback.Dispatcher;
import async.net.callback.DoExit;

public class SocketClient {
	public static void main(String[] args) throws IOException {
		final ASync async = new ASync();
		Dispatcher disp = async.createDispatcher();
		async.console().start(disp.createCallback());
		async.socket().connectTo("127.0.0.1", 12345, disp.createCallback(new DoExit("Server did close!")));
	}
}
