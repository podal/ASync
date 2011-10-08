package async.example.web;

import java.io.IOException;

import async.net.ASync;
import async.net.callback.HttpCallback;
import async.net.callback.PageAwareHttpCallback;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class WebPageExample {
	public static void main(String[] args) throws IOException {
		new ASync().http().listen(12345,  new PageAwareHttpCallback().add("/", new HttpCallback() {
			@Override
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				response.getWriter().println("Hello world [/]");
			}
		}).add("/test", new HttpCallback() {
			@Override
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				response.getWriter().println("Hello world [/test]");
			}
		}).addDefault(new HttpCallback() {
			@Override
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				response.getWriter().println("Default [" + request.getPath() + "]");
			}
		}));
	}
}
