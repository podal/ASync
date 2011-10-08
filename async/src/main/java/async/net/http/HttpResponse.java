package async.net.http;

import java.io.OutputStream;
import java.io.PrintWriter;

public interface HttpResponse {

	void setReturnCode(int i);

	OutputStream getOutputStream();

	PrintWriter getWriter();

	void sendRedirect(String url);

	void setHeader(String key, String value);

	void setEncoding(String string);

}
