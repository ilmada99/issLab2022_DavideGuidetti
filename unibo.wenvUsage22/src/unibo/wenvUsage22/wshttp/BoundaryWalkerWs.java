/*
ClientUsingHttp.java
*/
package unibo.wenvUsage22.wshttp;

import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;

import java.util.Observable;

import org.json.JSONObject;

import unibo.actor22comm.http.*;
import unibo.wenvUsage22.common.ApplData;

public class BoundaryWalkerWs implements IObserver {
	boolean obstacle = false;

	private Interaction2021 conn;

	protected void doJob() throws Exception {
		conn = WsConnection.create("localhost:8091" );
		((WsConnection)conn).addObserver(this);
		
		for (int i = 1; i <= 4; i++) {
			while (!obstacle) {
				conn.forward(ApplData.moveForward(500));
				ColorsOut.outappl(i + " number ", ColorsOut.BLACK);
				CommUtils.delay(500);
			}
			obstacle = false;
		}

	}

	@Override
	public void update(Observable source, Object data) {
		try {
			JSONObject d = new JSONObject(""+data);
			ColorsOut.outappl("BoundaryWalkerWs update collision=" + d.has("collision"), ColorsOut.MAGENTA);
			if (d.has("collision")){
				conn.forward(ApplData.stop());
				conn.forward(ApplData.turnLeft(300));
				CommUtils.delay(300);
				obstacle=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}

	@Override
	public void update(String data) {
		ColorsOut.out("ClientUsingWs update receives:" + data);
	}

	/*
	 * MAIN
	 */
	public static void main(String[] args) throws Exception {
		CommUtils.aboutThreads("Before start - ");
		new BoundaryWalkerWs().doJob();
		CommUtils.aboutThreads("At end - ");
	}

}
