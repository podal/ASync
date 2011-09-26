package async.net.http.impl;

import static async.net.http.HttpHeader.CONTENT_LENGTH;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import async.net.http.HTTPType;
import async.net.http.HttpRequest;
import async.net.impl.FixedInputStream;
import async.net.impl.IOUtil;

public class DefaultHttpRequest implements HttpRequest {
	private static final Map<Integer, List<String>> END_HEADER;

	private static final int MAX_HEADER_LENGTH = 10 * 1024;
	static {
		END_HEADER = new HashMap<Integer, List<String>>();
		END_HEADER.put(2, new ArrayList<String>(Arrays.asList("\n\n", "\r\r")));
		END_HEADER.put(4, new ArrayList<String>(Arrays.asList("\n\r\n\r", "\r\n\r\n")));
	}

	private InputStream stream;

	private HTTPType type;

	private String url;

	private Map<String, String> headers;

	private String queryString;

	private OutputStream outStream;

	public DefaultHttpRequest(InputStream stream) throws IOException, HttpError {
		this.stream = stream;
		parseStart(this.stream);
	}

	private void parseStart(InputStream stream2) throws IOException, HttpError {
		List<String> headers = new ArrayList<String>();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		int i;
		int last = 0;
		int newLineSize = 0;
		for (int pos = 0; (i = stream2.read()) != -1; pos++) {
			stream.write(i);
			if (i == 10 || i == 13) {
				newLineSize++;
				String string = stream.toString();
				String h = string.substring(last, pos).trim();
				if (!h.isEmpty()) {
					headers.add(h);
				}
				if (END_HEADER.containsKey(newLineSize)) {
					String end = string.substring(string.length() - newLineSize);
					if (END_HEADER.get(newLineSize).contains(end)) {
						break;
					}
				}
				last = pos;
			} else {
				newLineSize = 0;
			}
			if (pos >= MAX_HEADER_LENGTH) {
				throw new HttpError();
			}
		}
		parseFiretLine(headers);
		parseHeaders(headers);
	}

	private void parseHeaders(List<String> headers) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int j = 1; j < headers.size(); j++) {
			String string = headers.get(j);
			String[] strings = string.split(": ", 2);
			if (strings.length == 1) {
				map.put(string, "");
			} else {
				map.put(strings[0], strings[1]);
			}
		}
		this.headers = Collections.unmodifiableMap(map);
	}

	private void parseFiretLine(List<String> headers) throws HttpError {
		String[] s = headers.get(0).split(" ");
		if (s.length != 3) {
			throw new HttpError();
		}
		type = HTTPType.getType(s[0]);
		url = s[1];
		int q = s[1].indexOf('?');
		if (q != -1) {
			url = s[1].substring(0, q);
			queryString = s[1].substring(q + 1);
		}
	}

	@Override
	public HTTPType getType() {
		return type;
	}

	@Override
	public String getPath() {
		return url;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public String getQueryString() {
		return queryString;
	}

	@Override
	public void setOutputStream(OutputStream outStream) {
		this.outStream = outStream;
	}

	public void flush() throws IOException {
		String contentLength = getHeaders().get(CONTENT_LENGTH);
		if (contentLength != null) {
			stream = new FixedInputStream(stream, Integer.parseInt(contentLength));
		}
		if (outStream != null) {
			IOUtil.copy(stream, outStream);
			outStream.close();
		}
	}

}
