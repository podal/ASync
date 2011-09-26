package async.example.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import async.net.ASync;
import async.net.callback.HttpCallback;
import async.net.callback.PostParameterCollecter;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;
import async.net.velocity.VelocityHttpHandlerFactory;

public class WebServer {

	public static void main(String[] args) throws IOException {
		ASync aSync = new ASync();
		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		final VelocityHttpHandlerFactory handler = new VelocityHttpHandlerFactory("web").setDirDefault("index.vsl")
				.setEncoding("UTF-8");
		aSync.http().listen(8080, new HttpCallback() {
			@Override
			public void call(HttpRequest request, HttpResponse response) throws IOException {
				if (request.getType().isPost()) {
					request.setOutputStream(new PostParameterCollecter("UTF-8") {
						@Override
						public void requestFinish(Map<String, String> parameters) {
							synchronized (list) {
								list.add(parameters);
							}
						}
					});
					response.sendRedirect("/index.vsl");
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("info", list);
					response.setEncoding("UTF-8");
					handler.getHttpHandler(map).call(request, response);
				}
			}
		});
	}

}
