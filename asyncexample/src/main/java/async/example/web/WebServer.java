package async.example.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import async.net.ASync;
import async.net.callback.HttpCallback;
import async.net.callback.MethodAwareHttpCallback;
import async.net.callback.PostParameterCollecter;
import async.net.http.HTTPType;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;
import async.net.velocity.VelocityHttpHandlerFactory;
import async.net.velocity.VelocityMapFetcher;

public class WebServer {

	public static void main(String[] args) throws IOException {
		ASync aSync = new ASync();
		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		final VelocityHttpHandlerFactory handler = new VelocityHttpHandlerFactory("web").setDirDefault("index.vsl")
				.setEncoding("UTF-8");
		aSync.http().listen(8080, new MethodAwareHttpCallback().addPage(HTTPType.POST, new HttpCallback() {
			@Override
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				request.setOutputStream(new PostParameterCollecter("UTF-8") {
					@Override
					public void requestFinish(Map<String, String> parameters) {
						synchronized (list) {
							list.add(parameters);
						}
					}
				});
				response.sendRedirect("/index.vsl");
			}
		}).addDefault(handler.createCallback(new VelocityMapFetcher() {
			@Override
			public Map<String,Object> getMap(HttpRequest request, HttpResponse response) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("info", list);
				return map; 
			}
		})));
		
			}

}
