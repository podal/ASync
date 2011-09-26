package async.example.web;

import java.io.IOException;

import async.net.ASync;
import async.net.callback.HttpCallback;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class WebHelloWorld {
	public static void main(String[] args) throws IOException {
		new ASync().http().listen(8080, new HttpCallback() {
			@Override
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				response.getWriter().println("Hello world.");
			}
		});
	}
}
