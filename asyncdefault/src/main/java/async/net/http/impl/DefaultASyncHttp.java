package async.net.http.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import async.net.ASync;
import async.net.ASyncHttp;
import async.net.RemoteControll;
import async.net.callback.HttpCallback;
import async.net.callback.IOCallback;

public class DefaultASyncHttp implements ASyncHttp {

	private ASync aSync;

	public DefaultASyncHttp(ASync aSync) {
		this.aSync = aSync;
	}

	@Override
	public RemoteControll listen(int port, final HttpCallback callback) throws IOException {
		RemoteControll remote = aSync.socket().listenOn(port, new IOCallback() {
			@Override
			public void call(InputStream in, OutputStream out) throws IOException {
				DefaulHttpResponse out2 = new DefaulHttpResponse(out);
				try {
					DefaultHttpRequest in2 = new DefaultHttpRequest(in);
					callback.call(in2, out2);
					in2.flush();
					out2.flush();
				} catch (Throwable e) {
					e.printStackTrace();
					if (!out2.isFlush()) {
						out2.sendError();
					}
				} finally {
					out2.flush();
					out2.close();
				}
			}
		});
		return remote;
	}

}
