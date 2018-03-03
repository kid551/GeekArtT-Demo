package tomcat_web_dev;

import java.io.*;
import java.net.*;
import java.util.*;

public class HTTPServer1 {
	
	private static Map servletCache = new HashMap();
	
	/**
	 * It's like the Jupyter command, when you type
	 *     "jupyter 8081"
	 * the server application will be built at port 8081.
	 * 
	 * This is the same, when you type "java HTTPServer 8081",
	 * the server application will be built at port 8081.
	 * 
	 */
	public static void main(String args[]) {		
		int port;
		ServerSocket serverSocket;
		
		try {
			port = Integer.parseInt(args[0]);
		} catch ( Exception e) {
			System.out.println("port = 8080 (default)");
			port = 8080;
		}
		
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("The server is listening port: " + serverSocket.getLocalPort());
			
			while(true) {
				// Wait for client's TCP connection request
				final Socket socket = serverSocket.accept();
				System.out.println("A new TCP connection with client has been built, the client address is: " 
											+ socket.getInetAddress() + ":" + socket.getPort());
				try {
					service(socket);
				} catch (NullPointerException e) { System.out.println("The resource you request is NOT found!"); }
			}
		} catch(Exception e) { e.printStackTrace(); }
		
	}
	
	// Response to client's HTTP request
	/**
	 * @param socket
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void service(Socket socket) throws Exception {
		// Parse HTTP request information		
		String request = HTTPProtocolUtils.getHttpRequestInfo(socket);
		System.out.println(request);
		String uri = HTTPProtocolUtils.getURI(request);
		
		/**
		 * If the request wants to access Servlet, then call Servlet object's service() method dynamically.
		 * 
		 * For example, the servlet name of
		 *     "http://localhost:8080/servlet/HelloServlet?username=Tom"
		 * is
		 *     "HelloServlet",
		 *     
		 * which is between "servlet/" and "?". 
		 */
		if(uri.indexOf("servlet") != -1) {
			Servlet servlet = HTTPProtocolUtils.getServlet(
										HTTPProtocolUtils.getServletName(uri), 
										servletCache);
			
			// Call servlet's service() method
			servlet.service(request, socket.getOutputStream());
			
			// Wait for HTTP request
			Thread.sleep(1000);
			socket.close();
			return;
		}
		
		String contentType = HTTPProtocolUtils.getContentType(uri);		
		
		// Generate HTTP response information 		
		HTTPProtocolUtils.sendHttpResponse(uri, contentType, socket);
		
		Thread.sleep(1000);
		socket.close();
		
	}
}
