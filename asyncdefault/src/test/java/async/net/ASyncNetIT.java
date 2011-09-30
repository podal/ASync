package async.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import async.net.callback.IOCallback;

public class ASyncNetIT {
	private RemoteControl controll;

	@Test
	public void testSocket() throws InterruptedException, IOException {
		ASync async = new ASync();
		final List<String> server = new ArrayList<String>();
		final List<String> client = new ArrayList<String>();
		controll = async.socket().listenOn(12345, new IOCallback() {

			@Override
			public void call(InputStream in, OutputStream out) throws IOException {
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				PrintWriter writer = new PrintWriter(out);
				String line;
				while ((line = reader.readLine()) != null) {
					server.add(line);
					writer.println(new StringBuilder(line).reverse().toString());
					writer.flush();
				}
			}

		});
		async.socket().connectTo("127.0.0.1", 12345, new IOCallback() {

			@Override
			public void call(InputStream in, OutputStream out) throws IOException {
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				PrintWriter writer = new PrintWriter(out);
				String line;
				writer.println("Test");
				writer.flush();
				while ((line = reader.readLine()) != null) {
					client.add(line);
				}
			}

		});
		for (int i = 0; i < 200 && controll.isActive(); i++) {
			Thread.sleep(10);
		}
		controll.stop();
		Assert.assertEquals(1, server.size());
		Assert.assertEquals("Test", server.get(0));
		Assert.assertEquals(1, client.size());
		Assert.assertEquals("tseT", client.get(0));
	}

	public void stop() {
		if (controll != null) {
			controll.stop();
		}
	}
}
