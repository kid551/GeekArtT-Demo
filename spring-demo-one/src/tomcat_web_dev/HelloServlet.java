package tomcat_web_dev;

import java.io.OutputStream;

public class HelloServlet implements Servlet {

	@Override
	public void init() throws Exception {
		System.out.println("HelloServlet is initialized.");
		
	}

	@Override
	public void service(String request, OutputStream out) throws Exception {		
		String[] parts = HTTPProtocolUtils.getRequestParts(request);
		String method = parts[0];
		String uri = parts[1];
		
		String username = null;
		
		// If the method is "GET"
		if(method.equalsIgnoreCase("get") && uri.indexOf("username=") != -1) {
			/**
			 * Suppose uri = "servlet/HelloServlet?username=Tom&password=1234"
			 */
			// parameters = "username=Tom&password=1234"
			String parameters = uri.substring(uri.indexOf("?"), uri.length());
			
			parts = parameters.split("&");  // {"username=Tom", "password=1234"}
			parts = parts[0].split("=");  // {"username", "Tom"}
			username = parts[1];  // "Tom"			
		} 
		
		// If the method is "POST", the parameter is located in content
		if(method.equalsIgnoreCase("post")) {
			int pos = request.indexOf(System.lineSeparator() + System.lineSeparator());
			
			// Get response content
			String content = request.substring(System.lineSeparator().length() * 2, request.length());
			
			// Suppose content = "username=Tom&password=1234" 
			if(content.indexOf("username=") != -1) {
				parts = content.split("&");  // {"username=Tom", "password=1234"}
				parts = parts[0].split("=");  // {"username", "Tom"}
				username = parts[1];  // "Tom"	
			}
		}
		
		
		// Create and send HTTP request
		out.write(("HTTP/1.1 200 OK" + System.lineSeparator()).getBytes());
		out.write(("Content-Type: text/html" + System.lineSeparator() + System.lineSeparator()).getBytes());
		out.write(("<html>\n" + 
				"  <head>\n" + 
				"    <title>HelloWorld</title>\n" + 
				"  </head>\n" + 
				"  <body>").getBytes());
		out.write(new String("<h1>Hello:" + username + "</h1>\n" + 
				"  </body>\n" + 
				"</html>").getBytes());
		
	}

}
