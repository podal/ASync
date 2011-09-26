package async.net.http.impl;

import static async.net.http.HttpHeader.CONTENT_LENGTH;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseOutputStream extends OutputStream {

	private DefaulHttpResponse response;
	private OutputStream out;
	private ByteArrayOutputStream stream = new ByteArrayOutputStream();
	private boolean flush = false;
	private boolean close = false;
	private boolean started = false;

	public HttpResponseOutputStream(DefaulHttpResponse response, OutputStream out) {
		this.response = response;
		this.out = out;
	}

	@Override
	public void write(int b) throws IOException {
		checkClose();
		started = true;
		if (flush) {
			out.write(b);
		} else {
			stream.write(b);
		}
	}

	@Override
	public void write(byte[] bs, int s, int l) throws IOException {
		checkClose();
		started = true;
		if (flush) {
			out.write(bs, s, l);
		} else {
			stream.write(bs, s, l);
		}
	}

	@Override
	public void flush() throws IOException {
		checkClose();
		started = true;
		if (!flush) {
			out.write(response.getHead());
			out.write(stream.toByteArray());
			stream = null;
			flush = true;
		}
	}

	private void checkClose() throws IOException {
		if (close) {
			throw new IOException("Alredy closed");
		}
	}

	@Override
	public void close() throws IOException {
		if (!close) {
			if (!flush) {
				response.setHeader(CONTENT_LENGTH, Integer.toString(stream.size()));
			}
			flush();
			close = true;
		}
	}

	public boolean isFlush() {
		return flush;
	}

	public boolean isStarted() {
		return started;
	}

}
