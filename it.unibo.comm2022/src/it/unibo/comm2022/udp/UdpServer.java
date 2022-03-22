package it.unibo.comm2022.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.utils.ColorsOut;
import it.unibo.comm2022.utils.CommSystemConfig;

public class UdpServer extends Thread {
	private DatagramSocket socket;
	private byte[] buf;
	protected IApplMsgHandler userDefHandler;
	public Map<UdpEndpoint, UdpServerConnection> connectionsMap; // gestione socket client-server
	protected String name;
	protected boolean stopped = true;

	public UdpServer(String name, int port, IApplMsgHandler userDefHandler) {
		super(name);
		connectionsMap = new ConcurrentHashMap<UdpEndpoint, UdpServerConnection>();
		try {
			this.userDefHandler = userDefHandler;
			ColorsOut.out(getName() + " | costructor port=" + port, ColorsOut.BLUE);
			this.name = getName();
			socket = new DatagramSocket(port);
		} catch (Exception e) {
			ColorsOut.outerr(getName() + " | costruct ERROR: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			ColorsOut.out(getName() + " | STARTING ... ", ColorsOut.BLUE);
			buf = new byte[CommSystemConfig.max_len_udp];
			while (!stopped) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				UdpEndpoint client = new UdpEndpoint(address, port);
				UdpServerConnection conn = connectionsMap.get(client);
				if (conn == null) {
					conn = new UdpServerConnection(socket, client, connectionsMap);
					connectionsMap.put(client, conn);
				}
				conn.handle(packet);
				// Create a message handler on the connection
				new UdpApplMessageHandler(userDefHandler, conn);
			} // while
		} catch (Exception e) { // Scatta quando la deactive esegue: serversock.close();
			ColorsOut.out(getName() + " | probably socket closed: " + e.getMessage(), ColorsOut.GREEN);
			socket.close();
		}

	}

	public void activate() {
		if (stopped) {
			stopped = false;
			this.start();
		} // else already activated
	}

	public void deactivate() {
		try {
			ColorsOut.out(getName() + " |  DEACTIVATE serversock=" + socket);
			stopped = true;
			socket.close();
			connectionsMap.clear();
		} catch (Exception e) {
			ColorsOut.outerr(getName() + " | deactivate ERROR: " + e.getMessage());
		}
	}

}
