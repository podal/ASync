package async.net.http.impl;

import static async.net.http.HttpHeader.CONTENT_TYPE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import async.net.http.ASyncWriter;
import async.net.http.HttpResponse;

public class DefaulHttpResponse implements HttpResponse {

	private static final char[] NEW_LINE = { '\r', '\n' };
	private OutputStream out;
	private int code = 200;
	protected HttpResponseOutputStream responseOut;
	private Map<String, String> headers = new HashMap<String, String>();
	private DefalultASyncWriter writer;
	private String encoding = "ISO-8859-1";

	public DefaulHttpResponse(OutputStream out) {
		this.out = out;
		headers.put(CONTENT_TYPE, "text/plain");
	}

	@Override
	public void setReturnCode(int code) {
		this.code = code;
	}

	@Override
	public OutputStream getOutputStream() {
		if (responseOut == null) {
			responseOut = new HttpResponseOutputStream(this, out);
		}
		return responseOut;
	}

	public byte[] getHead() throws IOException {
		if (code == -1) {
			throw new IOException("Return code isn't set.");
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(stream);
		writer.print("HTTP/1.0 ");
		writer.print(code);
		writer.print(NEW_LINE);
		for (Map.Entry<String, String> en : headers.entrySet()) {
			writer.print(en.getKey());
			writer.print(": ");
			writer.print(en.getValue());
			writer.print(NEW_LINE);
		}

		writer.print(NEW_LINE);
		writer.flush();
		return stream.toByteArray();
	}

	public void setHeader(String key, String value) {
		headers.put(key, value);
	}

	public void close() throws IOException {
		if (writer != null) {
			writer.flush();
		}
		if (responseOut != null) {
			responseOut.close();
		}
	}

	public boolean isFlush() {
		if (responseOut != null) {
			return responseOut.isFlush();
		} else {
			return false;
		}
	}

	public void sendError() throws IOException {
		setReturnCode(500);
		OutputStream out = getOutputStream();
		try {
			out.write("Error 500".getBytes());
		} finally {
			out.close();
		}

	}

	@Override
	public ASyncWriter getWriter() {
		if (writer == null) {
			writer = new DefalultASyncWriter(getOutputStream(), encoding);
		}
		return writer;
	}

	@Override
	public void sendRedirect(String url) {
		if (isFlush() || (responseOut != null && responseOut.isStarted()) || (writer != null && writer.isStaerted())) {
			throw new IllegalStateException("Can't use 'sendRedirect' when started a stream/writer.");
		}
		setHeader("Location", url);
		setReturnCode(302);
	}

	public void flush() throws IOException {
		if (!isFlush()) {
			getOutputStream().flush();
		}
	}

	@Override
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
