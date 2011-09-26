package async.net.callback;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

public abstract class BufferedCharacterCallback extends CharacterCallback {

	public BufferedCharacterCallback(String charset) {
		super(charset);
	}

	public BufferedCharacterCallback(Charset charset) {
		super(charset);
	}

	@Override
	public final void call(Reader reader, Writer writer) throws IOException {
		call(new BufferedReader(reader), new BufferedWriter(writer));
	}

	public abstract void call(BufferedReader reader, BufferedWriter writer) throws IOException;

}
