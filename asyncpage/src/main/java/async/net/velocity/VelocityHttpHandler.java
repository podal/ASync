package async.net.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import async.net.http.AbstarctClassPathHttpHandler;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class VelocityHttpHandler extends AbstarctClassPathHttpHandler<ModelAndView> {

	private VelocityEngine engine;
	private Set<String> includeExtentions;
	private VelocityModelAndViewFetcher fetcher;
	private String encoding;

	VelocityHttpHandler(VelocityEngine engine, String pathPrefix, String dirDefault, Set<String> includeExtentions,
			VelocityModelAndViewFetcher fetcher, String encoding) {
		super(pathPrefix, dirDefault);
		this.engine = engine;
		this.includeExtentions = includeExtentions;
		this.fetcher = fetcher;
		this.encoding = encoding;
	}

	@Override
	protected void copy(HttpRequest request, HttpResponse response, String extention, InputStream stream)
			throws IOException {
		if (includeExtentions.contains(extention)) {
			Context context = new VelocityContext();
			Map<String, Object> model = fetcher.getModelAndView(request, response).getModel();
			response.setEncoding(encoding);
			for (Entry<String, Object> en : model.entrySet()) {
				context.put(en.getKey(), en.getValue());
			}
			engine.evaluate(context, response.getWriter(), getPathPrefix(), new InputStreamReader(stream, encoding));
		} else {
			super.copy(request, response, extention, stream);
		}
	}

	@Override
	protected String getView(ModelAndView modelView) {
		return modelView.getView();
	}

	@Override
	public ModelAndView getModelAndView(HttpRequest request, HttpResponse response) {
		return fetcher.getModelAndView(request, response);
	}

}
