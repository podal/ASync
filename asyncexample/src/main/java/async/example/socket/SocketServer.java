package async.example.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import async.net.ASync;
import async.net.callback.BufferedCharacterCallback;

public class SocketServer {
	public static void main(String[] args) throws IOException {
		ASync async = new ASync();
		async.socket().listenOn(12345, new BufferedCharacterCallback("utf-8") {
			@Override
			public void call(BufferedReader reader, BufferedWriter writer) throws IOException {
				writer.write("Welcome to remote reverse service, enter 'exit' to exit:");
				writer.newLine();
				writer.flush();
				String line;
				while ((line = reader.readLine()) != null && !line.equalsIgnoreCase("exit")) {
					writer.write(new StringBuilder(line).reverse().toString());
					writer.newLine();
					writer.flush();
				}
			}
		});
	}
}
