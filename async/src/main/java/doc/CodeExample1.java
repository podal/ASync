package doc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import async.net.ASync;
import async.net.ASyncType;
import async.net.RemoteControll;
import async.net.callback.BufferedCharacterCallback;
import async.net.callback.Dispatcher;
import async.net.callback.ExceptionCallback;
import async.net.callback.ExitCallback;
import async.net.callback.HttpCallback;
import async.net.callback.IOCallback;
import async.net.http.ASyncWriter;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;
import async.net.thread.ThreadHandler;

/**
 * This class is used for creating example in <code>sync.net.ASync</code>
 * JavaDoc class. Text sync is done by <b>CodeToJavaDoc</b>.
 * 
 * @see async.net.ASync
 */
public class CodeExample1 {
	int inLen;
	byte[] inBytes;
	private List<RemoteControll> remotes = new ArrayList<RemoteControll>();
	private IOCallback myIoCallback;
	private ExecutorService myExecutorService;

	private void startServer() throws IOException {
		// This creates a RemoteControll. To stop use remote.stop();
		RemoteControll remote = new ASync().socket().listenOn(12345, new BufferedCharacterCallback("UTF-8") {
			public void call(BufferedReader reader, BufferedWriter writer) throws IOException {
				String line;
				while ((line = reader.readLine()) != null) {
					writer.write(new StringBuffer(line).reverse().toString());
					writer.flush();
				}
			}
		});
		remotes.add(remote);
	}

	public void example() throws IOException, InterruptedException {
		try {
			startServer();
			startClient();
			startChatServer();
			startConsole();
			Thread.sleep(1000);
		} finally {
			stop();
		}
	}

	private void startChatServer() throws IOException {
		ASync aSync = new ASync();
		Dispatcher dispatcher = aSync.createDispatcher();
		remotes.add(aSync.socket().listenOn(12346, dispatcher.createFactory()));
	}

	private void startConsole() throws IOException {
		ASync aSync = new ASync();
		Dispatcher dispatcher = aSync.createDispatcher();
		aSync.console().start(dispatcher.createCallback());
		aSync.socket().connectTo("127.0.0.1", 12346, dispatcher.createCallback());
	}

	private void startClient() throws IOException {
		new ASync().socket().connectTo("127.0.0.1", 12345, new IOCallback() {
			public void call(InputStream in, OutputStream out) throws IOException {
				out.write("ASync\n".getBytes());
				// Streams can be buffered.
				out.flush();
				inBytes = new byte[1024];
				// read just one chunk.
				inLen = in.read(inBytes);
			}
		});
	}

	public void startReverseConsole() {
		new ASync().console().start(new IOCallback() {
			public void call(InputStream in, OutputStream out) throws IOException {
				byte[] bs = new byte[1024];
				int a;
				while ((a = in.read(bs)) != -1) {
					out.write(bs, 0, a);
					// Streams can be buffered.
					out.flush();
				}
			}
		});
	}

	private void stop() {
		for (RemoteControll remote : remotes) {
			try {
				remote.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void startWebServer() throws IOException {
		new ASync().http().listen(12347, new HttpCallback() {
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				ASyncWriter writer = response.getWriter();
				writer.write(request.getPath());
				writer.write(" ");
				writer.write(request.getQueryString());
				writer.flush();
			}
		});
	}

	public void doExceptionCallback() throws IOException {
		new ASync().exception(new ExceptionCallback<IOException>() {
			public void exception(IOException exception) {
				// ..deal with the Exception.
			}
		}).socket().listenOn(12348, myIoCallback);
	}

	public void exitHock() throws IOException {
		ASync aSync = new ASync();
		Dispatcher dispatcher = aSync.createDispatcher();
		aSync.console().start(dispatcher.createCallback());
		aSync.socket().connectTo("127.0.0.1", 12346, dispatcher.createCallback(new ExitCallback() {
			public void onExit() {
				System.exit(0);
			}
		}));
	}

	public void threadHandler() throws IOException {
		ASync aSync = new ASync();
		aSync.setHandler(new ThreadHandler() {
			public ExecutorService getExecutorService(ASyncType socketListen) {
				switch (socketListen) {
				case CONSOL_LISTEN:// ASyncType.CONSOL_LISTEN
					return Executors.newFixedThreadPool(1);
				default:
					return Executors.newCachedThreadPool();
				}
			}
		});
		aSync.console().start(myIoCallback);
	}

	public void executorService() throws IOException {
		ASync aSync = new ASync();
		aSync.setExecutor(myExecutorService);// Add custom ExecutorService
		aSync.console().start(myIoCallback);
	}
}