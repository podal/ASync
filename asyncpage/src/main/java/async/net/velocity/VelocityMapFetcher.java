package async.net.velocity;

import java.util.Map;

import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public interface VelocityMapFetcher {

	public Map<String, Object> getMap(HttpRequest request, HttpResponse response);

}
