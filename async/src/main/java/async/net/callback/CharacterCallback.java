package async.net.callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

public abstract class CharacterCallback implements IOCallback {

	private Charset charset;

	public CharacterCallback(String charset) {
		this(Charset.forName(charset));
	}
	
	public CharacterCallback(Charset charset) {
		this.charset = charset;
	}

	@Override
	public final void call(InputStream in, OutputStream out) throws IOException {
		call(new InputStreamReader(in,charset), new OutputStreamWriter(out,charset));
	}

	public abstract void call(Reader reader, Writer writer) throws IOException;

}
