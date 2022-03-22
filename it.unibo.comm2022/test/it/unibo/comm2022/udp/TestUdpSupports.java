package it.unibo.comm2022.udp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestUdpSupports {
	private UdpServer server;
	public static final int testPort = 8112;

	@Before
	public void up() {
	}

	@After
	public void down() {
		// if( server != null ) server.deactivate();
	}

	protected void startTheServer(String name) {
		server = new UdpServer(name, testPort, new NaiveApplHandler("naiveH"));
		server.activate();
	}
	
	protected void stopTheServer() {
		server.deactivate();		
	}

	@Test
	public void testSingleClient() {
		startTheServer("oneClientServer");
		// Create a connection
		new ClientDoingRequest().doWork("client1");
		System.out.println("tesSingleClient BYE");
		stopTheServer();
	}

	@Test
	public void testManyClients() {
		startTheServer("manyClientsServer");
		new ClientDoingRequest().doWork("client1");
		new ClientDoingRequest().doWork("client2");
		new ClientDoingRequest().doWork("client3");
		System.out.println("testManyClients BYE");
		stopTheServer();
	}

	private void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
