package async.net.callback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public abstract class PostParameterFetcher extends OutputStream {

	private ByteArrayOutputStream key = new ByteArrayOutputStream();
	private ByteArrayOutputStream value = new ByteArrayOutputStream();

	private Integer i = null;

	private OutputStream add = key;

	private String encoding;

	public PostParameterFetcher() {
		this("ISO-8859-1");
	}

	public PostParameterFetcher(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public void write(int i) throws IOException {
		if (this.i != null && this.i == -1) {
			this.i = i;
		} else if (this.i != null) {
			add.write((char) Integer.parseInt("" + (char) this.i.intValue() + (char) i, 16));
			this.i = null;
		} else if (i == '%') {
			this.i = -1;
		} else if (i == '&') {
			flushParam();
			add = key = new ByteArrayOutputStream();
			value = new ByteArrayOutputStream();
		} else if (i == '=') {
			add = value;
		} else {
			add.write(i);
		}
	}

	private void flushParam() {
		try {
			addParameter(key.toString(encoding), value.toString(encoding));
			key = new ByteArrayOutputStream();
			value = new ByteArrayOutputStream();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws IOException {
		if (key.size() + value.size() != 0) {
			flushParam();
			requestFinish();
		}
	}

	public abstract void requestFinish();

	public abstract void addParameter(String key, String value);
}
