package async.net;

/**
 * When starting a service you use this to stop and check if it's running.
 * 
 * <pre>
<!--Code start[doc.JavaDocExample1.remote] [B6B052E6820D98E2573AE0BB3B693769]-->
		ASyncHttp http = new ASync().http();
		RemoteControl remote = http.listen(8080, httpCallback);
		if (remote.isActive()) {
			remote.stop();
		}
<!--Code end-->
 * </pre>
 */
public interface RemoteControl {

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
