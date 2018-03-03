package tomcat_web_dev;

import java.io.OutputStream;

public interface Servlet {
	public void init() throws Exception;
	
	// Respond to HTTP request, generate concrete HTTP response result.
	public void service(String request, OutputStream out) throws Exception;
}
