package async.net.callback;

import java.io.IOException;

import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public abstract class MethodAwareHttpCallback implements HttpCallback {
	@Override
	public final void call(HttpRequest request, HttpResponse response) throws IOException {
		switch (request.getType()) {
		case GET:
			doGetCall(request, response);
			break;
		case POST:
			doPostCall(request, response);
			break;
		default:
			throw new RuntimeException("Can't handle type: " + request.getType());
		}
	}

	public abstract void doGetCall(HttpRequest request, HttpResponse response) throws IOException ;

	public abstract void doPostCall(HttpRequest request, HttpResponse response) throws IOException ;

}
