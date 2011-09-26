package async.net.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.Template;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.resource.ContentResource;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.ResourceManagerImpl;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import async.net.http.ClassPathHttpHandler;
import async.net.http.ResponseParser;
import async.net.http.ResponseParser.ResponseInfo;

public class ASyncResourceManager extends ResourceManagerImpl {

	private ClassPathHttpHandler handler;
	private RuntimeServices services;

	@Override
	public Resource getResource(String url, int type, final String encoding) throws ResourceNotFoundException,
			ParseErrorException {
		try {
			final List<InputStream> list = new ArrayList<InputStream>();
			final Resource contentResource;
			if (type == RESOURCE_CONTENT) {
				contentResource = new ContentResource();
			} else {
				contentResource = new Template();
			}
			ResponseParser.parse(url, new ResponseInfo() {
				@Override
				public void doNotFound(String path) throws IOException {
					throw new RuntimeException("Can't find path=" + path);
				}

				@Override
				public void doFile(String path, String extention, InputStream stream) throws IOException {
					list.add(stream);
				}

				@Override
				public void doDirectory(String path) {
				}

				@Override
				public String getPathPrefix() {
					return handler.getPathPrefix();
				}
			});
			contentResource.setResourceLoader(new ResourceLoader() {

				@Override
				public boolean isSourceModified(Resource arg0) {
					return true;
				}

				@Override
				public void init(ExtendedProperties arg0) {
				}

				@Override
				public InputStream getResourceStream(String arg0) throws ResourceNotFoundException {
					return list.isEmpty() ? null : list.get(0);
				}

				@Override
				public long getLastModified(Resource arg0) {
					return 0;
				}
			});
			contentResource.setLastModified(Long.MAX_VALUE);
			contentResource.setRuntimeServices(services);
			contentResource.setEncoding(encoding);
			contentResource.process();
			return contentResource;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized void initialize(RuntimeServices services) {
		super.initialize(services);
		this.services = services;
		handler = new ClassPathHttpHandler(services.getProperty("pathPrefix").toString());
	}
}
