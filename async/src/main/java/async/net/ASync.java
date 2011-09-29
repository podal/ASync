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
 * the work that needs to be done. All connection types (socket/console/web)
 * uses a similar approach, making it easy to switch between them.
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
 * <h3><a href="#1">1. Short Example</a></h3>
 * <h3><a href="#2">2. Dispatcher</a></h3>
 * <h3><a href="#3">3. Exceptions</a></h3>
 * <h3><a href="#4">4. Exist hooks</a></h3>
 * <h3><a href="#6">5. Threads</a></h3>
 * </p>
 * 
 * <h2 id="1">Short Example</h2>
 * <p>
 * Code says it all. Short snippets as example.
 * </p>
 * 
 * <b>Console</b>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startReverseConsole] [A74C90995C44CE6520B78B984C3105B6]-->
 * 		new ASync().console().start(new IOCallback() {
 * 			public void call(InputStream in, OutputStream out) throws IOException {
 * 				byte[] bs = new byte[1024];
 * 				int a;
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
 * <!--Code start[doc.CodeExample1.startClient] [B69DC5984FA07168B02F7C67EDF15293]-->
 * 		new ASync().socket().connectTo("127.0.0.1", 12345, new IOCallback() {
 * 			public void call(InputStream in, OutputStream out) throws IOException {
 * 				out.write("ASync\n".getBytes());
 * 				out.flush();// Force a flush if stream is buffered.
 * 				inBytes = new byte[1024];
 * 				// read just one chunk.
 * 				inLen = in.read(inBytes);
 * 			}// socket will be closed "automatically" (since out of scope)
 * 		});
 * <!--Code end-->
 * </pre>
 * 
 * <b>Listen on a Socket</b>
 * 
 * <pre>
 * <!--Code start[doc.CodeExample1.startServer] [2F919828A8657BC8D663694B0E164421]-->
 * 		// This creates a RemoteControll. To stop use remote.stop();
 * 		RemoteControll remote = new ASync().socket().listenOn(12345, new BufferedCharacterCallback("UTF-8") {
 * 			public void call(BufferedReader reader, BufferedWriter writer) throws IOException {
 * 				String line;
 * 				while ((line = reader.readLine()) != null) {
 * 					writer.write(new StringBuffer(line).reverse().toString());
 * 					writer.flush();// Force a flush if stream is buffered.
 * 				}
 * 			}// socket will be closed "automatically" (since out of scope)
 * 		});
 * 		remotes.add(remote);
 * <!--Code end-->
 * </pre>
 * 
 * <p>
 * Instead of using IOCallback we use BufferedCharacterCallback witch extends
 * CharacterCallback that implements IOCallback and we have a buffered Writer
 * and reader.
 * </p>
 * 
 * <b>Start web server</b>
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
 * <h2 id="2">Dispatcher</h2>
 * <p>
 * Dispatcher is used to dispatch requests. Dispatcher is a controller that
 * listen on inputStreams from all added callbacks and propagets it to all other
 * outputStreams.
 * </p>
 * 
 * <b>Connect console to a Socket</b>
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
 * <b>Connect connection to all other connection(Simple chat server)</b>
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
 * Now we use createFactory method and that will create a new IOCallback for
 * every request. When connecting to a socket or console
 * <code>IOCallback.call</code> method is only called ones and when listening on
 * a socket <code>IOCallback.call</code> method is called for each request
 * therefore we need a factory that creates new IOCallbacks.
 * </p>
 * <h2 id="3">3. Exceptions</h2>
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
 * <h2 id="4">4. Exist hooks on Dispatcher</h2>
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
 * 
 * <p>
 * This exit hook exit the program when socket has ended.
 * </p>
 * <h2 id="5">5. Threads</h2>
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
