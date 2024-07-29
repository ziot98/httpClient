package app.ssl;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

import app.HttpClient;

public class MyHandshakeCompletedListener implements HandshakeCompletedListener {
	@Override
	public void handshakeCompleted(HandshakeCompletedEvent event) {
	    SSLSession session = event.getSession();
	    String protocol = session.getProtocol();
	    String cipherSuite = session.getCipherSuite();
	    String peerName = null;
	
	    try {
	        peerName = session.getPeerPrincipal().getName();
	        HttpClient.toBase64SystemOut("* TLS Version : " + protocol);
	        HttpClient.toBase64SystemOut("* CipherSuite : " + cipherSuite);
	    } catch (SSLPeerUnverifiedException e) {
	    }
	}
}
