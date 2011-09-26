package async.net.http;

import java.io.OutputStream;
import java.util.Map;

public interface HttpRequest {

	HTTPType getType();

	String getPath();

	Map<String, String> getHeaders();

	String getQueryString();

	void setOutputStream(OutputStream out);

}
