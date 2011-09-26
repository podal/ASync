package async.net.callback;

public interface Dispatcher {

	IOCallback createCallback();

	IOCallback createCallback(ExitCallback exitCallback);

	IOCallback createFactory();

}
