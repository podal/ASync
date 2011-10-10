package async.net.velocity;

import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public interface VelocityModelAndViewFetcher {
 	ModelAndView getModelAndView(HttpRequest request, HttpResponse response);
}
