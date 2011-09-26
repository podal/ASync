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

import async.net.http.ClassPathHttpHandler;
import async.net.http.HttpResponse;

public class VelocityHttpHandler extends ClassPathHttpHandler {

	private VelocityEngine engine;
	private Set<String> includeExtentions;
	private Map<String, Object> model;
	private String encoding;

	VelocityHttpHandler(VelocityEngine engine, String pathPrefix, String dirDefault,
			Set<String> includeExtentions, Map<String, Object> model, String encoding) {
		super(pathPrefix, dirDefault);
		this.engine = engine;
		this.includeExtentions = includeExtentions;
		this.model = model;
		this.encoding = encoding;
	}

	@Override
	protected void copy(HttpResponse response, String extention, InputStream stream) throws IOException {
		if (includeExtentions.contains(extention)) {
			Context context = new VelocityContext();
			for (Entry<String, Object> en : model.entrySet()) {
				context.put(en.getKey(), en.getValue());
			}
			engine.evaluate(context, response.getWriter(), getPathPrefix(), new InputStreamReader(stream, encoding));
		} else {
			super.copy(response, extention, stream);
		}
	}

}
