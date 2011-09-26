package async.net.thread;

import java.util.concurrent.ExecutorService;

import async.net.ASyncType;

public class OneServiceThreadHandler implements ThreadHandler {

	private ExecutorService service;

	public OneServiceThreadHandler(ExecutorService service) {
		this.service = service;
	}

	@Override
	public ExecutorService getExecutorService(ASyncType socketListen) {
		return service;
	}

}
