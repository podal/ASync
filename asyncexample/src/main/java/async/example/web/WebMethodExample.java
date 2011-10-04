package async.example.web;

import java.io.IOException;
import java.util.Map;

import async.net.ASync;
import async.net.callback.MethodAwareHttpCallback;
import async.net.callback.PostParameterCollecter;
import async.net.http.HttpHeader;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class WebMethodExample {
	public static void main(String[] args) throws IOException {
		new ASync().http().listen(12345, new MethodAwareHttpCallback() {

			@Override
			public void doPostCall(HttpRequest request, final HttpResponse response) throws IOException {
				request.setOutputStream(new PostParameterCollecter("UTF-8") {
					@Override
					public void requestFinish(Map<String, String> parameters) {
						try {
							response.getWriter().print(parameters);
						} catch (IOException e) {
						}
					}
				});
			}

			@Override
			public void doGetCall(HttpRequest request, HttpResponse response) throws IOException {
				response.setHeader(HttpHeader.CONTENT_TYPE, "text/html");
				response.getWriter().print("<form method=\"post\"><input type=\"input\" name=\"name\"><input type=\"submit\"></form>");
			}
		});
	}
}
