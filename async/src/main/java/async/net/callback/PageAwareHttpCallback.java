package async.net.callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import async.net.http.HttpHeader;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class PageAwareHttpCallback implements HttpCallback {

	private Map<String, HttpCallback> pages = new HashMap<String, HttpCallback>();
	private HttpCallback defaultCallback;

	@Override
	public void call(HttpRequest request, HttpResponse response) throws IOException {
		HttpCallback httpCallback = pages.get(request.getPath());
		if (httpCallback != null) {
			httpCallback.call(request, response);
		} else if (defaultCallback != null) {
			defaultCallback.call(request, response);
		} else {
			response.setReturnCode(404);
			response.setHeader(HttpHeader.CONTENT_TYPE, "text/html");
			response.getWriter().println("<h1>404</h1>File Not found.");
		}
	}

	public PageAwareHttpCallback addPage(String page, HttpCallback httpCallback) {
		pages.put(page, httpCallback);
		return this;
	}

	public PageAwareHttpCallback addDefault(HttpCallback defaultCallback) {
		this.defaultCallback = defaultCallback;
		return this;
	}

}
