package async.net.thread;

import java.util.concurrent.ExecutorService;

import async.net.ASyncType;

public interface ThreadHandler {

	ExecutorService getExecutorService(ASyncType socketListen);

}
