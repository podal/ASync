package async.net.http;

public enum HTTPType {
	POST, GET, UNKNOWN;

	public static HTTPType getType(String typeAsString) {
		for (HTTPType type : values()) {
			if (type.toString().equalsIgnoreCase(typeAsString)) {
				return type;
			}
		}
		return UNKNOWN;
	}

	public boolean isPost() {
		return equals(POST);
	}

	public boolean isGet() {
		return equals(GET);
	}

	public boolean isUnknown() {
		return equals(UNKNOWN);
	}
}
