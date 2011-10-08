package async.net.callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import async.net.http.HttpHeader;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public abstract class AbstactAwareHttpCallback<T> implements HttpCallback {

	private Map<T, HttpCallback> pages = new HashMap<T, HttpCallback>();
	private HttpCallback defaultCallback;

	@Override
	public void call(HttpRequest request, HttpResponse response) throws IOException {
		HttpCallback httpCallback = pages.get(getKey(request, response));
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

	public abstract T getKey(HttpRequest request, HttpResponse response);
	
	public AbstactAwareHttpCallback<T> addPage(T page, HttpCallback httpCallback) {
		pages.put(page, httpCallback);
		return this;
	}

	public AbstactAwareHttpCallback<T> addDefault(HttpCallback defaultCallback) {
		this.defaultCallback = defaultCallback;
		return this;
	}

}
