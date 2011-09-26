package async.net.callback;

import java.util.HashMap;
import java.util.Map;

public abstract class PostParameterCollecter extends PostParameterFetcher {

	private Map<String, String> map = new HashMap<String, String>();

	public PostParameterCollecter() {
		super();
	}

	public PostParameterCollecter(String encoding) {
		super(encoding);
	}

	@Override
	public final void requestFinish() {
		requestFinish(map);
	}

	public abstract void requestFinish(Map<String, String> parameters);

	@Override
	public final void addParameter(String key, String value) {
		map.put(key, value);
	}

}
