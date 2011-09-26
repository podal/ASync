package async.net.impl;

import java.io.IOException;
import java.io.InputStream;

public class FixedInputStream extends InputStream {

	private InputStream stream;
	private int size;
	private int count = 0;

	public FixedInputStream(InputStream stream, int size) {
		this.stream = stream;
		this.size = size;
	}

	@Override
	public int read() throws IOException {
		if (count < size) {
			int i = stream.read();
			count++;
			return i;
		} else {
			return -1;
		}
	}

	@Override
	public int read(byte[] bs, int start, int length) throws IOException {
		if (count < size) {
			int i = stream.read(bs, start, Math.min(length, size - count));
			count += i;
			return i;
		} else {
			return -1;
		}
	}
}
