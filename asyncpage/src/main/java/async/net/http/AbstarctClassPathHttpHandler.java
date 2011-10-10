package async.net.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import async.net.callback.HttpCallback;
import async.net.http.ResponseParser.ResponseInfo;
import async.net.impl.IOUtil;

public abstract class AbstarctClassPathHttpHandler<MV> implements HttpCallback {

	public static final String INDEX_HTML = "index.html";

	private static final String DIR_EXTENTION_PROP_KEY = ".dir";
	private static final String DEFAULT_CONTENT_TYPE_PROP_KEY = ".default";
	private String pathPrefix;
	private String dirDefault;
	protected final static Map<String, String> extentionMap;
	protected final static String defaultContentType;
	protected final static String dirExtention;

	static {
		try {
			InputStream stream = ClassPathHttpHandler.class.getResourceAsStream("content-type.properties");
			if (stream == null) {
				throw new FileNotFoundException("Can't find content-type.properties in classpath.");
			}
			Properties properties = new Properties();
			properties.load(stream);
			Map<String, String> extentionMap2 = new HashMap<String, String>();
			copyProperties(properties, extentionMap2);
			extentionMap = Collections.unmodifiableMap(extentionMap2);
			defaultContentType = extentionMap.get(DEFAULT_CONTENT_TYPE_PROP_KEY).trim();
			dirExtention = extentionMap.get(DIR_EXTENTION_PROP_KEY).trim();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void copyProperties(Properties properties, Map<String, String> extentionMap2) {
		extentionMap2.putAll((Map) properties);
	}

	public AbstarctClassPathHttpHandler(String pathPrefix) {
		this(pathPrefix, INDEX_HTML);
	}

	public AbstarctClassPathHttpHandler(String pathPrefix, String dirDefault) {
		this.pathPrefix = pathPrefix;
		this.dirDefault = dirDefault;
	}

	@Override
	public void call(final HttpRequest request, final HttpResponse response) throws IOException {
		ResponseParser.parse(getModelAndView(request,response), new ResponseInfo<MV>() {

			@Override
			public void doDirectory(String path) {
				if (!path.endsWith("/")) {
					path = path + '/';
				}
				response.sendRedirect(path + dirDefault);
			}

			@Override
			public void doNotFound(String path) throws IOException {
				response.setReturnCode(404);
				response.setHeader(HttpHeader.CONTENT_TYPE, "text/html");
				response.getWriter().println("<h1>404</h1>File Not found.");
			}

			@Override
			public void doFile(String extention, InputStream stream) throws IOException {
				String contentType = extentionMap.get(extention);
				if (contentType == null) {
					contentType = defaultContentType;
				}
				response.setHeader(HttpHeader.CONTENT_TYPE, contentType);
				response.setReturnCode(200);
				copy(request,response, extention, stream);
			}

			@Override
			public String getPathPrefix() {
				return AbstarctClassPathHttpHandler.this.getPathPrefix();
			}

			@Override
			public String getView(MV modelView) {
				return AbstarctClassPathHttpHandler.this.getView(modelView);
			}
		});
	}

	protected abstract String getView(MV modelView);

	public abstract MV getModelAndView(HttpRequest request, HttpResponse response);
	
	protected void copy(HttpRequest request, HttpResponse response, String extention, InputStream stream) throws IOException {
		IOUtil.copy(stream, response.getOutputStream());
	}

	public String getPathPrefix() {
		return pathPrefix;
	}

}
