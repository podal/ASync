package async.net;

import java.io.IOException;

import async.net.callback.HttpCallback;

/**
 * Used to start ASync callback for HTTP(web).
 * 
 * <pre>
 * <!--Code start[doc.JavaDocExample1.aSyncHttp] [556222D9DC6FF30E0EB2C225C671D309]-->
 * 		ASyncHttp http = new ASync().http();
 * 		http.listen(8080, new HttpCallback() {
 * 			public void call(HttpRequest request, HttpResponse response) throws IOException {
 * 				// ...
 * 			}
 * 		});
 * <!--Code end-->
 * </pre>
 */
public interface ASyncHttp {

	RemoteControll listen(int port, HttpCallback callback) throws IOException;

}
