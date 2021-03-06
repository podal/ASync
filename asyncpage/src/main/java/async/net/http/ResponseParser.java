package async.net.http;

import java.io.IOException;
import java.io.InputStream;

public class ResponseParser {
	public interface ResponseInfo<MV> {
		void doDirectory(String path);

		void doFile(String extention, InputStream stream) throws IOException;

		void doNotFound(String path) throws IOException;

		String getPathPrefix();
		
		String getView(MV modelView);

	}

	public static <MV> void parse(MV modelView, ResponseInfo<MV> response2) throws IOException {
		String path = response2.getView(modelView).replace("..", "");
		if (!path.startsWith("/")) {
			path = '/' + path;
		}
		String classPath = response2.getPathPrefix() + path;
		int dot = classPath.lastIndexOf('.');
		String extention = "";
		if (dot != -1) {
			extention = classPath.substring(dot + 1);
		}
		if (extention.equals(ClassPathHttpHandler.dirExtention)) {
			response2.doDirectory(path);
		} else {
			InputStream stream;
			if ((stream = ClassLoader.getSystemResourceAsStream(classPath)) == null) {
				response2.doNotFound(path);
			} else {
				response2.doFile(extention, stream);
			}
		}
	}

}
