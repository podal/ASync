package async.net;

/**
 * When starting a service you use this to stop and check if it's running.
 * 
 * <pre>
 * <!--Code start[doc.JavaDocExample1.remote] [4B937102A2E32805916B30C000DE4937]-->
 * 		ASyncHttp http = new ASync().http();
 * 		RemoteControll remote = http.listen(8080, httpCallback);
 * 		if (remote.isActive()) {
 * 			remote.stop();
 * 		}
 * <!--Code end-->
 * </pre>
 */
public interface RemoteControll {

	/**
	 * Chack the status on the service.
	 * 
	 * @return true if service is running.
	 */
	boolean isActive();

	/**
	 * Stops the service.
	 */
	void stop();

}
