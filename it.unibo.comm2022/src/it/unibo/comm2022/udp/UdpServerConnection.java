package it.unibo.comm2022.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;
import java.util.concurrent.Semaphore;
import it.unibo.comm2022.utils.ColorsOut;
import it.unibo.comm2022.utils.CommSystemConfig;

public class UdpServerConnection extends UdpConnection {
	private Map<UdpEndpoint, UdpServerConnection> connMap;
	private DatagramPacket packet = null;
	private Semaphore waitToEnterNewPacket = new Semaphore(1); // handle() waits until a packet is still waiting for
																// being processed
	private Semaphore waitToConsumeNewPacket = new Semaphore(0); // receiveMsg() waits until a packet have arrived

	public UdpServerConnection(DatagramSocket socket, UdpEndpoint client,
			Map<UdpEndpoint, UdpServerConnection> connMap) throws Exception {
		super(socket, client);
		this.connMap = connMap;
	}

	@Override
	public void forward(String msg) throws Exception {
		try {
			byte[] buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, super.client.getAddress(), super.client.getPort());
			socket.send(packet);
		} catch (IOException e) {
			throw e;
		}
	}

	@Override
	public String receiveMsg() {
		String line;
		try {
			waitToConsumeNewPacket.acquire(); 
			if (closed && packet == null) {
				line = null; 
			} else {
				line = new String(packet.getData(), 0, packet.getLength());
				packet = null;
				if (line.equals(CommSystemConfig.close)) {
					close();
				}
			}
			waitToEnterNewPacket.release();
			return line;
		} catch (Exception e) {
			ColorsOut.outerr("UdpConnection | receiveMsg ERROR  " + e.getMessage());
			return null;
		}
	}

	@Override
	public void close() {
		connMap.remove(super.client);
		super.closed = true;
	}

	// handle packets that are received from server
	public void handle(DatagramPacket packet) {
		try {
			waitToEnterNewPacket.acquire();
			this.packet = packet;
			waitToConsumeNewPacket.release(); // enable receiveMsg()
		} catch (InterruptedException e) {
		}
	}

}