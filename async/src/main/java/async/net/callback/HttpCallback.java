package async.net.callback;

import java.io.IOException;

import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public interface HttpCallback {
	void call(HttpRequest request, HttpResponse response) throws IOException;

}
