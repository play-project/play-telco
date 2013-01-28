package com.orange.play.events.types.templates;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public final class CEPSender {
	public static final InetSocketAddress ORANGE_CEP_SOCKET_ADDRESS;
	
	public static final InetSocketAddress ORANGE_PROXY_SOCKET_ADDRESS;
		
	static {
		// Socket address resolution
		InetSocketAddress tempOrangeCEPSocketAddress = null;
		InetSocketAddress tempOrangeProxySocketAddress = null;
		try {			
			tempOrangeCEPSocketAddress = new InetSocketAddress("161.105.138.98", 3997);
			tempOrangeProxySocketAddress = new InetSocketAddress("s-proxy", 3128);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ORANGE_CEP_SOCKET_ADDRESS = tempOrangeCEPSocketAddress;
		ORANGE_PROXY_SOCKET_ADDRESS = tempOrangeProxySocketAddress;		
	}
	
	public static void send(InetSocketAddress cepSocketAddress, InetSocketAddress proxySocketAddress, String eventContent) throws IOException {
		Socket socket = null;
		try {
			socket = new Socket(cepSocketAddress.getAddress(), cepSocketAddress.getPort());
			DataOutputStream socketOutputStream = new DataOutputStream(socket.getOutputStream());
			socketOutputStream.writeBytes(eventContent + '\n');
		} catch (IOException e) {
			throw e;
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void sendCEPObject(InetSocketAddress cepSocketAddress, InetSocketAddress proxySocketAddress, CEPObject object) {
		try {
			send(cepSocketAddress, proxySocketAddress, object.toCEP());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
