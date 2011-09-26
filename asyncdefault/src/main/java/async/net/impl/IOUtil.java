package async.net.impl;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import async.net.callback.ExceptionCallback;

public final class IOUtil {

	private IOUtil() {
	}

	public static void close(Closeable closeable, ExceptionCallback<IOException> eCallback) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			if (eCallback != null) {
				eCallback.exception(e);
			}
		}
	}

	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] bs = new byte[1024];
		int i;
		while ((i = in.read(bs)) != -1) {
			out.write(bs, 0, i);
		}
	}

	public static void copy(Reader in, Writer out) throws IOException {
		char[] bs = new char[1024];
		int i;
		while ((i = in.read(bs)) != -1) {
			out.write(bs, 0, i);
		}
		out.flush();
	}
}
