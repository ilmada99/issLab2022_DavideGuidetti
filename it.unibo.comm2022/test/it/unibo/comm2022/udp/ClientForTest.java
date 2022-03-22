package it.unibo.comm2022.udp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.comm2022.utils.ColorsOut;

/*
 * A client that performs a request on a connnection woth the server 
 * and waits for the answer
 */
class ClientForTest {
	private Interaction2021 conn;

	public void doWorkWithServerOff(String name) {
		try {
			fail(); // non deve connttersi ...
		} catch (Exception e) {
			ColorsOut.outerr(name + " | ERROR (expected)" + e.getMessage());
		}
	}

	protected void connect() throws Exception {
		conn = UdpClientSupport.connect("localhost", TestUdpSupports.testPort);

	}

	public void doWorkWithServerOn(String name) {
		try {
			connect(); //1
			String request = "hello_from_" + name;
			conn.forward(request); // 2
			String answer = conn.receiveMsg(); // 3
			ColorsOut.out(name + " | receives the answer: " + answer);
			assertTrue(answer.equals("answerTo_" + request));
		} catch (Exception e) {
			ColorsOut.outerr(name + " | ERROR " + e.getMessage());
			fail();
		}
	}
}
