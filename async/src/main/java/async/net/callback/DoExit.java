package async.net.callback;

public class DoExit implements ExitCallback {

	private String msg;

	public DoExit() {
	}

	public DoExit(String msg) {
		this.msg = msg;
	}

	@Override
	public void onExit() {
		if (msg != null) {
			System.out.println(msg);
		}
		System.exit(0);
	}

}
