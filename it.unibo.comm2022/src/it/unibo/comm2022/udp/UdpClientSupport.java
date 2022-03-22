package it.unibo.comm2022.udp;

import java.net.DatagramSocket;
import java.net.InetAddress;

import it.unibo.comm2022.interfaces.Interaction2021;

public class UdpClientSupport {
    
    public static Interaction2021 connect(String serverAddress, int port ) throws Exception {
    	DatagramSocket socket  =  new DatagramSocket();
    	InetAddress address = InetAddress.getByName(serverAddress);
		Interaction2021 conn  =  new UdpConnection( socket,new UdpEndpoint(address,port));
		return conn;
 	}
    
}