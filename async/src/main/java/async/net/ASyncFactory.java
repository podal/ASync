package async.net;

import java.io.IOException;
import java.net.InetAddress;

import async.net.callback.Dispatcher;
import async.net.callback.ExceptionCallback;
import async.net.thread.ThreadHandler;

public interface ASyncFactory {

	ASyncSocket createASyncSocket(ThreadHandler handler, InetAddress address, ExceptionCallback<IOException> exceptionCallback);

	ASyncConsol createASyncConsol(ThreadHandler handler);

	Dispatcher createDispatcher();

	ASyncHttp createASyncHttp(ASync async, InetAddress address);

}
