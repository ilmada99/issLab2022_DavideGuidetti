package it.unibo.comm2022.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.comm2022.utils.ColorsOut;
import it.unibo.comm2022.utils.CommSystemConfig;

public class UdpConnection implements Interaction2021 {
	protected DatagramSocket socket;
	protected boolean closed;
	protected UdpEndpoint client;

	public UdpConnection(DatagramSocket socket, UdpEndpoint client) throws Exception {
		this.socket = socket;
		this.closed = false;
		this.client = client;
	}

	@Override
	public void forward(String msg) throws Exception {
		ColorsOut.out("UdpConnection | sendALine  " + msg + " on " + client.getAddress() + " " + client.getPort(),
				ColorsOut.ANSI_YELLOW);
		if (closed) {
			throw new Exception("The connection has been previously closed");
		}
		try {
			byte[] buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, client.getAddress(), client.getPort());
			socket.send(packet);
		} catch (IOException e) {
			throw e;
		}
	}

	@Override
	public String request(String msg) throws Exception {
		forward(msg);
		String answer = receiveMsg();
		return answer;
	}

	@Override
	public void reply(String msg) throws Exception {
		forward(msg);
	}

	@Override
	public String receiveMsg() {
		String line;
		try {
			if (closed) {
				line = null; // UdpApplMessageHandler will terminate
			} else {
				byte[] buf = new byte[CommSystemConfig.max_len_udp];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				line = new String(packet.getData(), 0, packet.getLength());
				packet = null;
			}
			return line;
		} catch (Exception e) {
			ColorsOut.outerr("UdpConnection | receiveMsg ERROR  " + e.getMessage());
			return null;
		}
	}

	@Override
	public void close() {
		byte[] buf = new byte[CommSystemConfig.close];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		closed = true;
		socket.close();
		ColorsOut.out("UdpConnection | CLOSED  ");
	}

}
