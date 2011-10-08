package async.net.callback;

import async.net.http.HTTPType;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class MethodAwareHttpCallback extends AbstactAwareHttpCallback<HTTPType> {

	@Override
	public HTTPType getKey(HttpRequest request, HttpResponse response) {
		return request.getType();
	}

}
