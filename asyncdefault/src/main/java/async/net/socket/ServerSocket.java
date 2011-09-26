package async.net.socket;

import java.io.Closeable;
import java.io.IOException;

public interface ServerSocket extends Closeable {

	Socket accept() throws IOException;

}
