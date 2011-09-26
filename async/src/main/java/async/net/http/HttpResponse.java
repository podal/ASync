package async.net.http;

import java.io.OutputStream;

public interface HttpResponse {

	void setReturnCode(int i);

	OutputStream getOutputStream();

	ASyncWriter getWriter();

	void sendRedirect(String url);

	void setHeader(String key, String value);

	void setEncoding(String string);

}
