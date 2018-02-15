package tomcat_web_dev;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class HTTPClient {
	public static void main(String[] args) {
		// Determine the uri of HTTP request
		String uri = "index.htm";
		if(0 != args.length) {
			uri = args[0];
		}
		
		doGet("localhost", 8080, uri);
	}
	
	// Access HTTPServer as "GET" method
	public static void doGet(String host, int port, String uri) {
		Socket socket = null;
		
		try {
			socket = new Socket(host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			/**
			 * Create and send HTTP request, e.g:
			 *     "GET /hello.html HTTP/1.1"
			 */
			StringBuffer httpContext = new StringBuffer(HTTPProtocolUtils.genRequestFirstLine(uri));
			HTTPProtocolUtils.addRequestHeader(httpContext);
			
			HTTPProtocolUtils.sendHttpRequest(socket, httpContext);
			
			Thread.sleep(2000);
			
			// Receive response result
			HTTPProtocolUtils.receiveHttpResponse(socket);			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}		
		
	}

}
