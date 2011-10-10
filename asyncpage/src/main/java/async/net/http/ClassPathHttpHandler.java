package async.net.http;

public final class ClassPathHttpHandler extends AbstarctClassPathHttpHandler<String> {

	public ClassPathHttpHandler(String pathPrefix, String dirDefault) {
		super(pathPrefix, dirDefault);
	}

	public ClassPathHttpHandler(String pathPrefix) {
		super(pathPrefix);
	}

	@Override
	protected String getView(String modelView) {
		return modelView;
	}

	@Override
	public String getModelAndView(HttpRequest request, HttpResponse response) {
		return request.getPath();
	}


}
