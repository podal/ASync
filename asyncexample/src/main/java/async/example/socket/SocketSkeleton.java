package async.example.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import async.net.ASync;
import async.net.callback.IOCallback;

public class SocketSkeleton {
	public static void main(String[] args) throws IOException {
		new ASync().socket().listenOn(12345, new IOCallback() {
			@Override
			public void call(InputStream in, OutputStream out) throws IOException {
			}
		});
	}
}
