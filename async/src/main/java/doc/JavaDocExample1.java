package doc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import async.net.ASync;
import async.net.ASyncConsol;
import async.net.ASyncHttp;
import async.net.ASyncSocket;
import async.net.RemoteControl;
import async.net.callback.HttpCallback;
import async.net.callback.IOCallback;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class JavaDocExample1 {
	private HttpCallback httpCallback;

	public void aSyncConsol() {
		ASyncConsol console = new ASync().console();
		console.start(new IOCallback() {
			public void call(InputStream in, OutputStream out) throws IOException {
				// ...
			}
		});
	}

	public void aSyncSocket() throws IOException {
		ASyncSocket socket = new ASync().socket();
		socket.connectTo("127.0.0.1", 12345, new IOCallback() {
			public void call(InputStream in, OutputStream out) throws IOException {
				// ...
			}
		});
	}

	public void aSyncHttp() throws IOException {
		ASyncHttp http = new ASync().http();
		http.listen(8080, new HttpCallback() {
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				// ...
			}
		});
	}

	public void remote() throws IOException {
		ASyncHttp http = new ASync().http();
		RemoteControl remote = http.listen(8080, httpCallback);
		if (remote.isActive()) {
			remote.stop();
		}
	}
}
