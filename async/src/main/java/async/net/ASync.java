package async.net;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import async.net.callback.Dispatcher;
import async.net.callback.ExceptionCallback;
import async.net.thread.OneServiceThreadHandler;
import async.net.thread.ThreadHandler;

/**
 * <h1>ASync</h1> ASync is asyncronius library to deal with asyncrone
 * communication: Socket, Console and Web. <h2>Content</h2>
 * <p>
 * <h3><a href="#1">1. Console</a></h3>
 * <b> <a href="#1.1">1.1. Use console</a><br/>
 * </b>
 * <h3><a href="#2">2. Socket</a></h3>
 * <b> <a href="#2.1">2.1. Connect to a socket</a><br/>
 * <a href="#2.2">2.2. Start a responding socket</a><br/>
 * <a href="#2.3">2.3. Connect to a socket and copy all to and from console</a><br/>
 * <a href="#2.4">2.4. Chat server</a><br/>
 * </b>
 * <h3><a href="#3">3. WebServer</a></h3>
 * <b> <a href="#3.1">3.1. Start a WebServer</a><br/>
 * <a href="#3.2">3.2. Make a guest book</a><br/>
 * </b>
 * <h3><a href="#4">4. Exceptions</a></h3>
 * <h3><a href="#5">5. Exist hooks</a></h3>
 * <h3><a href="#6">6. Threads</a></h3>
 * </p>
 * 
 * <h2 id="1">Console</h2> <h3 id="1.1">1.1. Use console</h3> Send back the
 * entered characters.
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startReverseConsole] [783D6FCEAEDEE90C28C0ADE9262028B5]-->
 * 		new ASync().console().start(new IOCallback() {
 * 			public void call(InputStream in, OutputStream out) throws IOException {
 * 				byte[] bs = new byte[1024];
 * 				int a;
 * 				while ((a = in.read(bs)) != -1) {
 * 					out.write(bs, 0, a);
 * 					// Streams can be buffered.
 * 					out.flush();
 * 				}
 * 			}
 * 		});
 * <!--Code end-->
 * </pre>
 * 
 * We use an IOCallback and listening on input stream and send it to output
 * stream. <h2 id="2">Socket</h2> <h3 id="2.1">2.1. Connect to a socket</h3>
 * Connect to a socket and writes 'ASync' and prints the result to the StdOut.
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startClient] [6E34EFCE8F520D5AD3991A6F4CCA22A2]-->
 * 		new ASync().socket().connectTo("127.0.0.1", 12345, new IOCallback() {
 * 			public void call(InputStream in, OutputStream out) throws IOException {
 * 				out.write("ASync\n".getBytes());
 * 				// Streams can be buffered.
 * 				out.flush();
 * 				inBytes = new byte[1024];
 * 				// read just one chunk.
 * 				inLen = in.read(inBytes);
 * 			}
 * 		});
 * <!--Code end-->
 * </pre>
 * 
 * Opens a socket and do the work and then close it.
 * 
 * <h3 id="2.2">2.2. Connect to a socket</h3> Start a server socket and send the
 * fetched text back in reverse order.<br/>
 * Instead of using IOCallback we use BufferedCharacterCallback witch extends
 * CharacterCallback that implements IOCallback and we have a buffered Writer
 * and reader.
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startServer] [4401C53B7EC0FE1CA7F40C94193F3E12]-->
 * 		// This creates a RemoteControll. To stop use remote.stop();
 * 		RemoteControll remote = new ASync().socket().listenOn(12345, new BufferedCharacterCallback("UTF-8") {
 * 			public void call(BufferedReader reader, BufferedWriter writer) throws IOException {
 * 				String line;
 * 				while ((line = reader.readLine()) != null) {
 * 					writer.write(new StringBuffer(line).reverse().toString());
 * 					writer.flush();
 * 				}
 * 			}
 * 		});
 * 		remotes.add(remote);
 * <!--Code end-->
 * </pre>
 * 
 * When exit the call method all resources are cleaned. <h3 id="2.3">2.3.
 * Connect to a socket and copy all to and from console</h3>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startConsole] [177545C5BC22BBAF830C8EF1A50B3779]-->
 * 		ASync aSync = new ASync();
 * 		Dispatcher dispatcher = aSync.createDispatcher();
 * 		aSync.console().start(dispatcher.createCallback());
 * 		aSync.socket().connectTo("127.0.0.1", 12346, dispatcher.createCallback());
 * <!--Code end-->
 * </pre>
 * 
 * Now we introduce a Dispatcher. Dispatcher is a controller that listen on
 * inputStreams from all added callbacks and propagets it to all other
 * outputStreams. We add a callback in console and socket.
 * 
 * <h3 id="2.4">2.4. Chat server</h3> Starts a chat server that copying all
 * information that one client sends to all other clients.
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startChatServer] [59DF404164DA7FDDB20264F9573AD08A]-->
 * 		ASync aSync = new ASync();
 * 		Dispatcher dispatcher = aSync.createDispatcher();
 * 		remotes.add(aSync.socket().listenOn(12346, dispatcher.createFactory()));
 * <!--Code end-->
 * </pre>
 * 
 * We use Dispatcher to dispatch requests. Now we use createFactory method and
 * that will create a new IOCallback for every request. When connecting to a
 * socket or console <code>IOCallback.call</code> method is only called ones and
 * when listening on a socket <code>IOCallback.call</code> method is called for
 * each request therefore we need a factory that creates new IOCallbacks for
 * each call. <h2 id="3">3. WebServer</h2><h3 id="3.1">3.1. Start a WebServer</h3>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startWebServer] [51B253AC00EA458B0784E935F9588BC0]-->
 * 		new ASync().http().listen(12347, new HttpCallback() {
 * 			public void call(HttpRequest request, HttpResponse response) throws IOException {
 * 				ASyncWriter writer = response.getWriter();
 * 				writer.write(request.getPath());
 * 				writer.write(" ");
 * 				writer.write(request.getQueryString());
 * 				writer.flush();
 * 			}
 * 		});
 * <!--Code end-->
 * </pre>
 * 
 * Starts a WebServer. On a request it return path and query string as result.
 * <h3 id="3.2">3.2. Make a guest book</h3>
 * <p>
 * <a href="page.html">See extension: <b>Page</b></a>
 * <p>
 * <h2 id="4">4. Exceptions</h2>
 * <p>
 * In ASync there are two types of exceptions that can be thrown. One when
 * starting of a resource(Socket, ServerSocket and WebServer) and one that can
 * be thrown when a service is operating such as problem with communication or
 * start a client connection.
 * </p>
 * <p>
 * The first one when starting a resource is a checked exception and will be
 * added on the starting method. The reason for that is when starting a resource
 * you don't wont to check a implemented callback interface to make sure your
 * resource is started, we believe you want a quick and accurate response.
 * </p>
 * 
 * <p>
 * The second on error when service is working. There is a ExceptionCallback
 * that can be added as a parameter to start method.
 * </p>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.doExceptionCallback] [731FD155402E700263C3AAE3236E314A]-->
 * 		new ASync().exception(new ExceptionCallback<IOException>() {
 * 			public void exception(IOException exception) {
 * 				// ..deal with the Exception.
 * 			}
 * 		}).socket().listenOn(12348, myIoCallback);
 * <!--Code end-->
 * </pre>
 * 
 * <h2 id="5">5. Exist hooks on Dispatcher</h2>
 * <p>
 * To create a exit hook on on a dispatcher callback you just add a
 * ExitCallback. Method onExit is called when call in callback has exit.
 * </p>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.exitHock] [23D7C8BE7B96C391189890CA2BB8E79E]-->
 * 		ASync aSync = new ASync();
 * 		Dispatcher dispatcher = aSync.createDispatcher();
 * 		aSync.console().start(dispatcher.createCallback());
 * 		aSync.socket().connectTo("127.0.0.1", 12346, dispatcher.createCallback(new ExitCallback() {
 * 			public void onExit() {
 * 				System.exit(0);
 * 			}
 * 		}));
 * <!--Code end-->
 * </pre>
 * <p>
 * This exit hook exit the program when socket has ended.
 * </p>
 * <h2 id="6">6. Threads</h2>
 * <p>
 * By default <code>Executors.newCachedThreadPool()</code> is used, however you
 * can customise witch ExecutorService ASync uses by adding ExecutorService.
 * </p>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.executorService] [680B3714197998B5115B987BC33D90F2]-->
 * 		ASync aSync = new ASync();
 * 		aSync.setExecutor(myExecutorService);// Add custom ExecutorService
 * 		aSync.console().start(myIoCallback);
 * <!--Code end-->
 * </pre>
 * <p>
 * Example above uses same service for all type of recourses, to specify
 * different service for different recourses use ThreadHandler.
 * </p>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.threadHandler] [7D4BC2B361484125471B2E93DADAF7FC]-->
 * 		ASync aSync = new ASync();
 * 		aSync.setHandler(new ThreadHandler() {
 * 			public ExecutorService getExecutorService(ASyncType socketListen) {
 * 				switch (socketListen) {
 * 				case CONSOL_LISTEN:// ASyncType.CONSOL_LISTEN
 * 					return Executors.newFixedThreadPool(1);
 * 				default:
 * 					return Executors.newCachedThreadPool();
 * 				}
 * 			}
 * 		});
 * 		aSync.console().start(myIoCallback);
 * <!--Code end-->
 * </pre>
 * 
 * @see async.net.velocity.VelocityHttpHandlerFactory
 * @see async.net.callback.ExceptionCallback
 * @see async.net.callback.ExitCallback
 * @see async.net.thread.ThreadHandler
 */

public final class ASync {
	private ThreadHandler handler;

	private ExceptionCallback<IOException> exceptionCallback = null;

	private InetAddress address;

	private static ASyncFactory factory;

	static {
		try {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Class<ASyncFactory> factoryClass = (Class) Class.forName("async.net.ASyncFactoryImpl");
			factory = factoryClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see ASync
	 */
	public ASync() {
		setExecutor(Executors.newCachedThreadPool());
	}

	/**
	 * @see ASyncSocket
	 */
	public ASyncSocket socket() {
		return factory.createASyncSocket(handler,address, exceptionCallback);
	}

	/**
	 * @see ASyncHttp
	 */
	public ASyncHttp http() {
		return factory.createASyncHttp(this,address);
	}

	/**
	 * Set the new ThreadHandler that ASync will use when creating threads.
	 */
	public ASync setHandler(ThreadHandler handler) {
		this.handler = handler;
		return this;
	}

	/**
	 * Set the new ExecutorService that ASync will use when creating threads.
	 */
	public ASync setExecutor(ExecutorService service) {
		return setHandler(new OneServiceThreadHandler(service));
	}

	/**
	 * @see ASyncConsol
	 */
	public ASyncConsol console() {
		return factory.createASyncConsol(handler);
	}

	/**
	 * @see Dispatcher
	 */
	public Dispatcher createDispatcher() {
		return factory.createDispatcher();
	}

	public ASync exception(ExceptionCallback<IOException> exceptionCallback) {
		this.exceptionCallback = exceptionCallback;
		return this;
	}

	public ASync address(InetAddress address) {
		this.address = address;
		return this;
	}
}
