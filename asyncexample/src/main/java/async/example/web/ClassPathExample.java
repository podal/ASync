package async.example.web;

import java.io.IOException;

import async.net.ASync;
import async.net.http.ClassPathHttpHandler;

public class ClassPathExample {

	public static void main(String[] args) throws IOException {
		new ASync().http().listen(8080, new ClassPathHttpHandler("web", "index.html"));
	}

}
