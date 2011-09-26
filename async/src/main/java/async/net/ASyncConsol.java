package async.net;

import java.io.IOException;

import async.net.callback.ExceptionCallback;
import async.net.callback.IOCallback;

/**
 * Used to start ASync callback to console.
 * 
 * <pre>
 * <!--Code start[doc.JavaDocExample1.aSyncConsol] [2897DEF2DC1FB0B799E04C82F92A53F6]-->
 * 		ASyncConsol console = new ASync().console();
 * 		console.start(new IOCallback() {
 * 			public void call(InputStream in, OutputStream out) throws IOException {
 * 				// ...
 * 			}
 * 		});
 * <!--Code end-->
 * </pre>
 */
public interface ASyncConsol {

	/**
	 * Start a ASync console with callback as handler.
	 * 
	 * @param callback
	 *            IOCallback to be used for ASync console.
	 */
	void start(IOCallback callback);

	/**
	 * Start a ASync console with callback as handler and eCallback as Exception
	 * handler.
	 * 
	 * @param callback
	 *            IOCallback to be used for ASync console.
	 * @param exceptionCallback
	 *            callback for all IOException.
	 */
	void start(IOCallback callback, ExceptionCallback<IOException> exceptionCallback);

}
