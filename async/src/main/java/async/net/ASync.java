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
 * <h1>ASync</h1>
 * <p>
 * ASync is an asynchronous library to handle asynchronous communication over
 * sockets, console and web. It provides an easy to use interface, simplifying
 * the work that needs to be done. All connection types (socket/console/web) use
 * a similar approach, making it easy to switch between them.
 * </p>
 * 
 * <p>
 * The purpose of ASync is to simplify a tedious task, and to make it swifter to
 * develop debugging interfaces.
 * </p>
 * 
 * <p>
 * The ASync library uses the Factory design pattern, among others.
 * </p>
 * <p>
 * <h3><a href="#1">1. Short examples</a></h3>
 * <h3><a href="#2">2. Dispatcher</a></h3>
 * <h4>&nbsp;<a href="#2.1">2.1. Exist hooks</a></h4>
 * <h3><a href="#3">3. Exceptions</a></h3>
 * <h3><a href="#4">4. Threads</a></h3>
 * </p>
 * 
 * <h2 id="1">Short examples</h2>
 * <p>
 * Some short examples to illustrate how to use the different interfaces of the
 * ASync library.
 * </p>
 * 
 * <b>Console</b>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startReverseConsole] [F25775CAEB33B2EF26714CB49B8A8864]-->
 * 		new ASync().console().start(new IOCallback() {//Create new console
 * 			public void call(InputStream in, OutputStream out) throws IOException {
 * 				byte[] bs = new byte[1024];
 * 				int a;
 * 				//Get data from console input and send it to console output.
 * 				while ((a = in.read(bs)) != -1) {
 * 					out.write(bs, 0, a);				
 * 					out.flush();// Force a flush if stream is buffered. 
 * 				}
 * 			}
 * 		});
 * <!--Code end-->
 * </pre>
 * 
 * <b>Connect to socket</b>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startClient] [D9B707856BA81F3907F4A59329D9F360]-->
 * 		new ASync().socket().connectTo("127.0.0.1", 12345, new IOCallback() {// Connect to host 127.0.0.1 on port 12345
 * 			public void call(InputStream in, OutputStream out) throws IOException {
 * 				// This is just nonsense example code
 * 				out.write("ASync\n".getBytes());//Sends a "ASync" to server
 * 				out.flush();// Force a flush if stream is buffered.
 * 				inBytes = new byte[1024];
 * 				// read just one chunk.
 * 				inLen = in.read(inBytes);
 * 				System.out.println(new String(inBytes,0,inLen,"UTF-8"));
 * 			}// socket will be closed "automatically" (since out of scope)
 * 		});
 * <!--Code end-->
 * </pre>
 * 
 * <b>Listen on a socket</b>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startServer] [2BCB90DCDD7EEC8FFEF1A568F6A3386F]-->
 * 		// This creates a RemoteControl. To stop use remote.stop();
 * 		RemoteControl remote = new ASync().socket().listenOn(12345,//Start listening on socket. RemoteControl makes it possible from outside of ASync API to monitoring and close down service
 * 				new BufferedCharacterCallback("UTF-8") {
 * 			public void call(BufferedReader reader, BufferedWriter writer) throws IOException {
 * 				String line;
 * 				while ((line = reader.readLine()) != null) {//Read a line
 * 					writer.write(new StringBuffer(line).reverse().toString());//Send the read line back to client
 * 					writer.flush();// Force a flush if stream is buffered.
 * 				}
 * 			}// socket will be closed "automatically" (since out of scope)
 * 		});
 * 		remotes.add(remote);
 * <!--Code end-->
 * </pre>
 * 
 * <p>
 * When starting a listener or a webserver it return a remote control. It's used
 * to check status of listener/webserver or close it.
 * </p>
 * <p>
 * In this example BufferedCharacterCallback is used. Thats a type of IOCallback
 * that has a buffered writer/reader.
 * </p>
 * 
 * <b>Start web server</b>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startWebServer] [257A2A072787C8F45A9D5EA447505F21]-->
 * 		new ASync().http().listen(12347, new HttpCallback() {// Start a web server that listening on port 12347
 * 			public void call(HttpRequest request, HttpResponse response) throws IOException {
 * 				// This is just nonsense example code
 * 				ASyncWriter writer = response.getWriter();// Get a writer to response client.
 * 				writer.write(request.getPath());// Get requested path and send it to client.
 * 				writer.write(" ");
 * 				writer.write(request.getQueryString());// Gets query string and send it to client.
 * 				writer.flush();
 * 			}
 * 		});
 * <!--Code end-->
 * </pre>
 * 
 * <p>
 * 
 * </p>
 * 
 * <h2 id="2">2. Dispatcher</h2>
 * <p>
 * The Dispatcher is used to dispatch requests. A dispatcher is a controller
 * that listen on inputStreams from all added callbacks and propagaters it to
 * all other outputStreams.
 * </p>
 * 
 * <b>Connect console to a socket</b>
 * <p>
 * Next example is a simple client to a chat server.
 * </p>
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
 * <b>Connect one connection to all other connections(Simple chat server)</b>
 * <p>
 * Next example is a simple chat server.
 * </p>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startChatServer] [59DF404164DA7FDDB20264F9573AD08A]-->
 * 		ASync aSync = new ASync();
 * 		Dispatcher dispatcher = aSync.createDispatcher();
 * 		remotes.add(aSync.socket().listenOn(12346, dispatcher.createFactory()));
 * <!--Code end-->
 * </pre>
 * 
 * <p>
 * In this example createFactory method is used which will create a new
 * IOCallback for every request. When connecting to a socket or console
 * <code>IOCallback.call</code> is only called once. When listening on a socket
 * <code>IOCallback.call</code> is called for each request. Because of this, a
 * factory is needed that creates new IOCallback for each new call.
 * </p>
 * <h3 id="2.1">2.1. Exist hooks on Dispatcher</h3>
 * <p>
 * Exit hook is called when a dispatchers IOCallback has exit. Thats can be used
 * for writing a message or exit a program when a recource has closed down. To
 * create a exit hook on on a dispatcher callback you just add a ExitCallback.
 * </p>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.exitHock] [888ABB27B8DC212B572F3668DFAC751D]-->
 * 		ASync aSync = new ASync();
 * 		Dispatcher dispatcher = aSync.createDispatcher();
 * 		aSync.console().start(dispatcher.createCallback());
 * 		aSync.socket().connectTo("127.0.0.1", 12346, dispatcher.createCallback(new ExitCallback() {
 * 			public void onExit() {//called when socket has close
 * 				System.exit(0);
 * 			}
 * 		}));
 * <!--Code end-->
 * </pre>
 * 
 * <h2 id="3">3. Exceptions</h2>
 * <p>
 * In ASync library there are two types of exceptions:
 * <ul>
 * <li>Problem when starting of a resource(Socket, ServerSocket and WebServer)</li>
 * <li>Problem when a service is operating such as problem with communication or
 * start a client connection.</li>
 * </ul>
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
 * <h2 id="4">4. Threads</h2>
 * <p>
 * By default <code>Executors.newCachedThreadPool()</code> is used, however you
 * can customize witch ExecutorService ASync uses by adding ExecutorService.
 * </p>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.executorService] [680B3714197998B5115B987BC33D90F2]-->
 * 		ASync aSync = new ASync();
 * 		aSync.setExecutor(myExecutorService);// Add custom ExecutorService
 * 		aSync.console().start(myIoCallback);
 * <!--Code end-->
 * </pre>
 * 
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
		return factory.createASyncSocket(handler, address, exceptionCallback);
	}

	/**
	 * @see ASyncHttp
	 */
	public ASyncHttp http() {
		return factory.createASyncHttp(this, address);
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
