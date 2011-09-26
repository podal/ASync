package async.net.impl;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import async.net.ASyncConsol;
import async.net.ASyncType;
import async.net.callback.ExceptionCallback;
import async.net.callback.IOCallback;
import async.net.thread.ThreadHandler;

public class DefaultASyncConsol implements ASyncConsol {

	private ThreadHandler handler;

	public DefaultASyncConsol(ThreadHandler handler) {
		this.handler = handler;
	}

	@Override
	public void start(IOCallback callback) {
		start(callback, null);
	}

	@Override
	public void start(final IOCallback callback, final ExceptionCallback<IOException> eCallback) {
		ExecutorService ex = handler.getExecutorService(ASyncType.CONSOL_LISTEN);
		ex.execute(new Runnable() {

			@Override
			public void run() {
				try {
					callback.call(System.in, System.out);
				} catch (IOException e) {
					if (eCallback != null) {
						eCallback.exception(e);
					}
				}
			}
		});
	}
}
