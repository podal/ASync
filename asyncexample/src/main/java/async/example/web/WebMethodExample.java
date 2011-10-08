package async.example.web;

import java.io.IOException;
import java.util.Map;

import async.net.ASync;
import async.net.callback.HttpCallback;
import async.net.callback.MethodAwareHttpCallback;
import async.net.callback.PostParameterCollecter;
import async.net.http.HTTPType;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class WebMethodExample {
	public static void main(String[] args) throws IOException {
		new ASync().http().listen(12345, new MethodAwareHttpCallback().addPage(HTTPType.GET, new HttpCallback() {
			@Override
			public void call(HttpRequest request, final HttpResponse response) throws IOException {
				request.setOutputStream(new PostParameterCollecter("UTF-8") {
					@Override
					public void requestFinish(Map<String, String> parameters) {
						response.getWriter().println(parameters);
					}
				});
			}
		}).addDefault(new HttpCallback() {			
			@Override
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				response.getWriter().print(
						"<form method=\"post\"><input type=\"input\" name=\"name\"><input type=\"submit\"></form>");
			}
		}));
	}
}
