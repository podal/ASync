package async.net.velocity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import async.net.callback.HttpCallback;
import async.net.http.ClassPathHttpHandler;
import async.net.http.HttpRequest;
import async.net.http.HttpResponse;

public class VelocityHttpHandlerFactory {

	private VelocityEngine engine;

	private static final Set<String> DEFAULT_INCLUDE_EXTENTION = Collections.unmodifiableSet(new HashSet<String>(Arrays
			.asList("vsl")));

	private Set<String> includeExtentions = DEFAULT_INCLUDE_EXTENTION;

	private String pathPrefix;

	private String dirDefault = ClassPathHttpHandler.INDEX_HTML;

	private String encoding = "ISO-8859-1";

	public VelocityHttpHandlerFactory(String pathPrefix) {
		this.pathPrefix = pathPrefix;
		engine = new VelocityEngine();
		engine.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
		engine.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		engine.setProperty(RuntimeConstants.RESOURCE_MANAGER_CLASS, ASyncResourceManager.class.getName());
		engine.setProperty("pathPrefix", pathPrefix);
		engine.init();
	}

	public VelocityHttpHandlerFactory setDirDefault(String dirDefault) {
		this.dirDefault = dirDefault;
		return this;
	}

	public VelocityHttpHandlerFactory setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public VelocityHttpHandlerFactory setIncludeExtentions(Set<String> includeExtentions) {
		this.includeExtentions = Collections.unmodifiableSet(new HashSet<String>(includeExtentions));
		return this;
	}

	public HttpCallback getHttpHandler(final Map<String, Object> objects) {
		return new VelocityHttpHandler(engine, pathPrefix, dirDefault, includeExtentions, new VelocityMapFetcher() {
			@Override
			public Map<String, Object> getMap(HttpRequest request, HttpResponse response) {
				return objects;
			}
		}, encoding);
	}

	public HttpCallback createCallback(VelocityModelAndViewFetcher fetcher) {
		return new VelocityHttpHandler(engine, pathPrefix, dirDefault, includeExtentions, fetcher, encoding);
	}

}
