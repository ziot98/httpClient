package app.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SecureSSLSocketFactory extends SSLSocketFactory {
private final SSLSocketFactory delegate;
private HandshakeCompletedListener handshakeListener;

public SecureSSLSocketFactory(
        SSLSocketFactory delegate, HandshakeCompletedListener handshakeListener) {
    this.delegate = delegate;
    this.handshakeListener = handshakeListener;
}

	@Override
	public Socket createSocket(Socket s, String host, int port, boolean autoClose) 
	    throws IOException {
	    SSLSocket socket = (SSLSocket) this.delegate.createSocket(s, host, port, autoClose);
	
	    if (null != this.handshakeListener) {
	        socket.addHandshakeCompletedListener(this.handshakeListener);
	    }
	
	    return socket;
	}
	
	@Override
	public String[] getDefaultCipherSuites() {
	    return this.delegate.getDefaultCipherSuites();
	}
	
	@Override
	public String[] getSupportedCipherSuites() {
	    return this.delegate.getSupportedCipherSuites();
	}
	
	@Override
	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		return this.delegate.createSocket(host, port);
	}
	
	@Override
	public Socket createSocket(String host, int port, InetAddress localHost,
			int localPort) throws IOException, UnknownHostException {
		return this.delegate.createSocket(host, port, localHost, localPort);
	}
	
	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return this.delegate.createSocket(host, port);
	}
	
	@Override
	public Socket createSocket(InetAddress address, int port,
			InetAddress localAddress, int localPort) throws IOException {
		return this.delegate.createSocket(address, port, localAddress, localPort);
	}
}
