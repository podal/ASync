package async.net.callback;

import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class PageAwareHttpCallback extends AbstactAwareHttpCallback<String> {

	@Override
	public String getKey(HttpRequest request, HttpResponse response) {
		return request.getPath();
	}

}
