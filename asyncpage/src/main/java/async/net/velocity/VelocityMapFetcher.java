package async.net.velocity;

import java.util.Map;

import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public abstract class VelocityMapFetcher implements VelocityModelAndViewFetcher {

	@Override
	public final ModelAndView getModelAndView(HttpRequest request, HttpResponse response) {
		return new ModelAndView(request.getPath(), getMap(request, response));
	}

	public abstract Map<String, Object> getMap(HttpRequest request, HttpResponse response);
}
