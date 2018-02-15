package tomcat_web_dev;

import java.io.*;
import java.net.*;

public class HTTPServer {
	
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
				service(socket);
			}
		} catch(Exception e) { e.printStackTrace(); }
		
	}
	
	// Response to client's HTTP request
	public static void service(Socket socket) throws Exception {
		// Parse HTTP request information		
		String request = HTTPProtocolUtils.getHttpRequestInfo(socket);
		System.out.println(request);		
		String uri = HTTPProtocolUtils.getURI(request);		
		String contentType = HTTPProtocolUtils.getContentType(uri);		
		
		// Generate HTTP response information 		
		HTTPProtocolUtils.sendHttpResponse(uri, contentType, socket);
		
		Thread.sleep(1000);
		socket.close();
		
	}
}
