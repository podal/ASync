package async.net;

import java.io.IOException;

import async.net.callback.HttpCallback;
import async.net.http.ASyncWriter;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class AsyncMain {

	public static void main(String[] args) throws IOException {
		new ASync().http().listen(8080, new HttpCallback() {
			@Override
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				response.setReturnCode(200);
				ASyncWriter writer = response.getWriter();
				writer.println("Test");
				writer.println();
				writer.println(request.getType());
				writer.println(request.getPath());
				writer.println(request.getQueryString());
				writer.println(request.getHeaders());
				writer.close();
			}
		});
	}
}
