package async.net.http.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import async.net.http.ASyncWriter;

public class DefalultASyncWriter extends ASyncWriter {

	private static final char[] LINE_SEPARATOR = { '\n' };
	private Writer writer;
	private boolean started = false;

	public DefalultASyncWriter(OutputStream outputStream, String encoding) {
		this(toWriter(outputStream, encoding));
	}

	private static Writer toWriter(OutputStream outputStream, String encoding) {
		try {
			return new OutputStreamWriter(outputStream, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public DefalultASyncWriter(Writer writer) {
		this.writer = writer;
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void write(char[] chars, int start, int end) throws IOException {
		started = true;
		writer.write(chars, start, end);
	}

	public void print(Object obj) throws IOException {
		started = true;
		if (obj == null) {
			write("null");
		} else {
			write(obj.toString());
		}
	}

	public void println(Object obj) throws IOException {
		print(obj);
		println();
	}

	public void println() throws IOException {
		write(LINE_SEPARATOR, 0, LINE_SEPARATOR.length);
	}

	public boolean isStaerted() {
		return started;
	}
}
