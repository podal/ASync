package async.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import async.net.callback.Dispatcher;
import async.net.callback.ExitCallback;
import async.net.callback.IOCallback;

public class DefaultDispatcher implements Dispatcher {
	private Set<OutputStream> streams = new HashSet<OutputStream>();

	@Override
	public IOCallback createCallback() {
		return createCallback(null);
	}

	@Override
	public IOCallback createCallback(final ExitCallback exitCallback) {
		return new IOCallback() {
			@Override
			public void call(InputStream in, OutputStream out) throws IOException {
				try {
					addStream(out);
					int i;
					byte[] bs = new byte[1024];
					while ((i = in.read(bs)) != -1) {
						write(bs, 0, i, out);
					}
				} finally {
					if (exitCallback != null) {
						exitCallback.onExit();
					}
					removeStream(out);
				}
			}
		};
	}

	protected synchronized void removeStream(OutputStream stream) {
		streams.remove(stream);
	}

	protected synchronized void write(byte[] bs, int i, int i2, OutputStream... excludeStreams) throws IOException {
		List<OutputStream> excludeStreamsAsList = Arrays.asList(excludeStreams);
		for (OutputStream stream : streams) {
			if (!excludeStreamsAsList.contains(stream)) {
				stream.write(bs, i, i2);
				stream.flush();
			}
		}
	}

	private synchronized void addStream(OutputStream stream) {
		streams.add(stream);
	}

	@Override
	public IOCallback createFactory() {
		return new IOCallback() {

			@Override
			public void call(InputStream in, OutputStream out) throws IOException {
				createCallback().call(in, out);
			}
		};
	}

}
