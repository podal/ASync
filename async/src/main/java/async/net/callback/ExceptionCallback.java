package async.net.callback;

public interface ExceptionCallback<T extends Throwable> {
	void exception(T exception);
}
