package async.net.callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IOCallback {
	void call(InputStream in, OutputStream out) throws IOException;
}
