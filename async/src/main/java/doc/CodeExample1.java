package doc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import async.net.ASync;
import async.net.ASyncType;
import async.net.RemoteControl;
import async.net.callback.BufferedCharacterCallback;
import async.net.callback.Dispatcher;
import async.net.callback.ExceptionCallback;
import async.net.callback.ExitCallback;
import async.net.callback.HttpCallback;
import async.net.callback.IOCallback;
import async.net.callback.MethodAwareHttpCallback;
import async.net.callback.PageAwareHttpCallback;
import async.net.callback.PostParameterCollecter;
import async.net.http.HTTPType;
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
	private List<RemoteControl> remotes = new ArrayList<RemoteControl>();
	private IOCallback myIoCallback;
	private ExecutorService myExecutorService;

	private void startServer() throws IOException {
		// This creates a RemoteControl. To stop use remote.stop();
		RemoteControl remote = new ASync().socket().listenOn(12345,//Start listening on socket. RemoteControl makes it possible from outside of ASync API to monitoring and close down service
				new BufferedCharacterCallback("UTF-8") {
			public void call(BufferedReader reader, BufferedWriter writer) throws IOException {
				String line;
				while ((line = reader.readLine()) != null) {//Read a line
					writer.write(new StringBuffer(line).reverse().toString());//Send the read line back to client
					writer.flush();// Force a flush if stream is buffered.
				}
			}// socket will be closed "automatically" (since out of scope)
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
		new ASync().socket().connectTo("127.0.0.1", 12345, new IOCallback() {// Connect to host 127.0.0.1 on port 12345
			public void call(InputStream in, OutputStream out) throws IOException {
				// This is just nonsense example code
				out.write("ASync\n".getBytes());//Sends a "ASync" to server
				out.flush();// Force a flush if stream is buffered.
				inBytes = new byte[1024];
				// read just one chunk.
				inLen = in.read(inBytes);
				System.out.println(new String(inBytes,0,inLen,"UTF-8"));
			}// socket will be closed "automatically" (since out of scope)
		});
	}

	public void startReverseConsole() {
		new ASync().console().start(new IOCallback() {//Create new console
			public void call(InputStream in, OutputStream out) throws IOException {
				byte[] bs = new byte[1024];
				int a;
				//Get data from console input and send it to console output.
				while ((a = in.read(bs)) != -1) {
					out.write(bs, 0, a);				
					out.flush();// Force a flush if stream is buffered. 
				}
			}
		});
	}

	private void stop() {
		for (RemoteControl remote : remotes) {
			try {
				remote.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void startWebServer() throws IOException {
		new ASync().http().listen(12347, new HttpCallback() {// Start a web server that listening on port 12347
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				// This is just nonsense example code
				PrintWriter writer = response.getWriter();// Get a writer to response client.
				writer.write(request.getPath());// Get requested path and send it to client.
				writer.write(" ");
				writer.write(request.getQueryString());// Gets query string and send it to client.
				writer.flush();
			}
		});
	}

	public void webServerPath() throws IOException {
		new ASync().http().listen(12348,new PageAwareHttpCallback().// Start a web server that listening on port 12348
		addPage("/", new HttpCallback() {//Add httpCallback for page '/'
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				PrintWriter writer = response.getWriter();
				writer.write("StartPage");
				writer.flush();				
			}
		}).
		addPage("/page2", new HttpCallback() {//Add httpCallback for page 'page2'
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				PrintWriter writer = response.getWriter();
				writer.write("Page2");
				writer.flush();				
			}
		}).addDefault(new HttpCallback() {//Add httpCallback for all other page
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				response.setReturnCode(404);
				PrintWriter writer = response.getWriter();
				writer.write(String.format("File '%s' not found.", request.getPath()));
				writer.flush();				
			}
		}));
	}

	public void webServerMethod() throws IOException {
		new ASync().http().listen(12349,new MethodAwareHttpCallback()
		.addPage(HTTPType.POST, new HttpCallback() {
			public void call(HttpRequest request, final HttpResponse response) throws IOException {
				request.setOutputStream(new PostParameterCollecter("UTF-8") {
					public void requestFinish(Map<String, String> parameters) {// Called when request is done.
							response.getWriter().print(parameters);
					}
				});
			}
		}).addDefault(new HttpCallback() {
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				response.getWriter().print("<form method=\"post\"><input type=\"input\" name=\"name\"><input type=\"submit\"></form>");
			}
		}));
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
			public void onExit() {//called when socket has close
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
