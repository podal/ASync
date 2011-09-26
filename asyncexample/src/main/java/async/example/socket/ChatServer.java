package async.example.socket;

import java.io.IOException;

import async.net.ASync;
import async.net.callback.Dispatcher;

public class ChatServer {
	public static void main(String[] args) throws IOException {
		ASync async = new ASync();
		Dispatcher disp = async.createDispatcher();
		async.socket().listenOn(12345, disp.createFactory());
	}
}
