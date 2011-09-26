package async.net.http;

import java.io.IOException;
import java.io.Writer;

public abstract class ASyncWriter extends Writer {

	public abstract void print(Object obj) throws IOException;

	public abstract void println(Object obj) throws IOException;

	public abstract void println() throws IOException;
}
