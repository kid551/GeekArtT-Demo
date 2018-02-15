package tomcat_web_dev;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPProtocolUtils {
	public static String getHttpRequestInfo(Socket socket) throws Exception {	
		/**
		 * Why do we have to use bytes and stream? 
		 * 
		 * Because the infrastructure of information expression, the base thing is "byte", 
		 * thus "byte" is the "common part" of all expression.
		 * 
		 * Then, the input/output stream provide a common interface for more
		 * general service.
		 * 
		 * So in all, the input/output stream, the byte array are two
		 * unavoidable parts.
		 */
		InputStream socketIn = socket.getInputStream();
		Thread.sleep(500);
		byte[] buffer = new byte[socketIn.available()];
		socketIn.read(buffer);
		
		return new String(buffer);
	}
	
	public static String[] getRequestParts(String request) {
		// Parse HTTP request
		String firstLineOfRequest = request.substring(0, request.indexOf( System.lineSeparator() ));
		return firstLineOfRequest.split(" ");		
	}
	
	public static String getURI(String request) {		
		/*
		 * Based on the protocol of HTTP, we know the "second part" is the URI.
		 * 
		 * For example:
		 *     "POST /hello.jsp HTTP/1.1"
		 * the second part is URI: /hello.jsp
		 *  
		 */
		int uriPos = 1;
		
		return getRequestParts(request)[uriPos];
	}
	
	public static String getContentType(String uri) {
		String contentType = "";
		
		/* Determine the type of HTTP respond type, such as
		 * "html, jsp, jpg, gif" etc,. 
		 */
		if(uri.indexOf("html") != -1 || uri.indexOf("htm") != -1) {
			contentType = "text/html";
		} else if (uri.indexOf("jpg") != -1 || uri.indexOf("jpeg") != -1) {
			contentType = "image/jpeg";
		} else if (uri.indexOf("gif") != -1) {
			contentType = "image/gif";
		} else {
			contentType = "application/octet-stream";	// byte stream type
		}
		
		return contentType;
	}
	
	public static String genResponseFirstLine() {
		return "HTTP/1.1 200 OK" + System.lineSeparator();
	}
	
	public static String genResponseHeader(String contentType) {
		return "Content-Type: " + contentType + System.lineSeparator() + System.lineSeparator();
	}
	
	public static InputStream getResponseContextStream(String uri) {
		// Get resource from the directory of "HTTPServer.class" file,
		// then its deeper trace is "<resPos>/uri"
		String resPos = "resRoot";
		return HTTPServer.class.getResourceAsStream(resPos + "/" + uri);
	}
	
	public static void sendHttpResponse(String uri, String contentType, Socket socket) throws Exception {
		OutputStream socketOut = socket.getOutputStream();
		
		// 1. Response first line
		socketOut.write(genResponseFirstLine().getBytes());
		// 2. Response header
		socketOut.write(genResponseHeader(contentType).getBytes());		
		// 3. Response context
		InputStream in = getResponseContextStream(uri);
		int len = 0;
		byte[] buffer = new byte[128];
		while((len = in.read(buffer)) != -1) {
			socketOut.write(buffer, 0, len);
		}
	}
	
	public static String genRequestFirstLine(String uri) {
		return "GET " + uri + " HTTP/1.1" + System.lineSeparator();
	}
	
	public static void addRequestHeader(StringBuffer requestSB) {
		requestSB.append("Accept: */*" + System.lineSeparator());
		requestSB.append("Accept-Language: zh-cn" + System.lineSeparator());
		requestSB.append("Accept-Encoding: gzip, deflate" + System.lineSeparator());
		requestSB.append("User-Agent: HTTPClient" + System.lineSeparator());
		requestSB.append("Host: localhost:8080" + System.lineSeparator());
		requestSB.append("Connection: Keep-Alive" + System.lineSeparator() + System.lineSeparator());
	}
	
	public static void sendHttpRequest(Socket socket, StringBuffer context) throws Exception{
		OutputStream socketOut = socket.getOutputStream();
		socketOut.write(context.toString().getBytes());
	}
	
	public static void receiveHttpResponse(Socket socket) throws Exception{
		InputStream socketIn = socket.getInputStream();
		byte[] buffer = new byte[socketIn.available()];
		socketIn.read(buffer);
		System.out.println(new String(buffer));	
	}
	
	
}
