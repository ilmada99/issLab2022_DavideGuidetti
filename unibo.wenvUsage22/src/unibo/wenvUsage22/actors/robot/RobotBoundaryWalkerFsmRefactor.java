package unibo.wenvUsage22.actors.robot;

import org.json.JSONException;
import org.json.JSONObject;
import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22Fsm;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.ws.WsConnSysObserver;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.common.ApplData;
import unibo.wenvUsage22.common.VRobotMoves;

public class RobotBoundaryWalkerFsmRefactor extends QakActor22Fsm {
	private Interaction2021 conn;
	private int i;

	public RobotBoundaryWalkerFsmRefactor(String name) {
		super(name);
		i = 0;
	}

	@Override
	protected void declareTheStates() {

		declareState("start", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo("" + msg);
				conn = WsConnection.create("localhost:8091");
				((WsConnection) conn).addObserver(new WsConnSysObserver(getName()));
				addTransition("goingAhead", ApplData.robotCmdId);
				nextState();
			}
		});
		declareState("goingAhead", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo("" + msg);
				VRobotMoves.step(getName(), conn);
				String payload = msg.msgContent().replaceAll("'", "");// remove ' '
				outInfo("PAYLOAD " + payload);
				try {
					JSONObject json = new JSONObject(payload);
					outInfo("" + json);
					boolean b = false;
					boolean c = false;
					outInfo("num iteration " + i);
					if (i < 5) {
						if (json.has("endmove")) {
							b = json.getBoolean("endmove");
						}
						if (json.has("collision")) {
							addTransition("turnedLeft", "wsEvent");
						} else if (b) {
							addTransition("goingAhead", "wsEvent");
						}

					}
				} catch (JSONException e) {
					addTransition("goingAhead", "wsEvent");
				}

				nextState();
			}
		});

		declareState("turnedLeft", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo("" + msg);
				i++;
				if (i < 5) {
					VRobotMoves.turnLeft(getName(), conn);

					addTransition("goingAhead", "wsEvent");
					nextState();

				}
			}
		});
	}

	@Override
	protected void setTheInitialState() {
		declareAsInitialState("start");
	}

}
