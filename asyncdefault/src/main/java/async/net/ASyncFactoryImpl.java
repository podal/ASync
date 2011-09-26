package async.net;

import java.io.IOException;
import java.net.InetAddress;

import async.net.callback.Dispatcher;
import async.net.callback.ExceptionCallback;
import async.net.http.impl.DefaultASyncHttp;
import async.net.impl.DefaultASyncConsol;
import async.net.impl.DefaultASyncSocket;
import async.net.impl.DefaultDispatcher;
import async.net.thread.ThreadHandler;

public class ASyncFactoryImpl implements ASyncFactory {

	@Override
	public ASyncSocket createASyncSocket(ThreadHandler handler, InetAddress address, ExceptionCallback<IOException> exceptionCallback) {
		return new DefaultASyncSocket(handler,address, exceptionCallback);
	}

	@Override
	public ASyncHttp createASyncHttp(ASync async, InetAddress address) {
		return new DefaultASyncHttp(async);
	}

	@Override
	public ASyncConsol createASyncConsol(ThreadHandler handler) {
		return new DefaultASyncConsol(handler);
	}

	@Override
	public Dispatcher createDispatcher() {
		return new DefaultDispatcher();
	}

}
