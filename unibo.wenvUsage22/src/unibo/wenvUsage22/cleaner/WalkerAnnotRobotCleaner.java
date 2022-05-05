package unibo.wenvUsage22.cleaner;

import org.json.JSONObject;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Context;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.annot.walker.WsConnWEnvObserver;
import unibo.wenvUsage22.common.ApplData;
import unibo.wenvUsage22.common.VRobotMoves;

public class WalkerAnnotRobotCleaner extends QakActor22FsmAnnot {
	private Interaction2021 conn;
	private int ncorner = 0;
	private int x = 0;
	private int y = 0;
	private int contPassi = 0;
	private int rotation = 0;
	boolean wotkInterrupted = false;
	boolean turnDir = true;
	String currentMove = "none";

	public WalkerAnnotRobotCleaner(String name) {
		super(name);
	}

	@State(name = "robotStart", initial = true)
	@Transition(state = "robotMovingY", msgId = SystemData.endMoveOkId)
	@Transition(state = "wallDetected", msgId = SystemData.endMoveKoId)
	protected void robotStart(IApplMessage msg) {
		outInfo("" + msg + " connecting (blocking all the actors ) ... ");
		conn = WsConnection.create("localhost:8091");
		outInfo("connected " + conn);
		((WsConnection) conn).addObserver(new WsConnWEnvObserver(getName()));
		new Sentinel().start();
		VRobotMoves.step(getName(), conn);
		y++;
		contPassi++;
		currentMove = "step";
	}

	@State(name = "robotMovingY")
	@Transition(state = "robotMovingY", msgId = SystemData.endMoveOkId)
	@Transition(state = "wallDetected", msgId = SystemData.endMoveKoId) // potrebbe non incrementare ncorner
	protected void robotMovingY(IApplMessage msg) {
		VRobotMoves.step(getName(), conn);
		y++;
		contPassi++;
		currentMove = "step";
	}

	@State(name = "robotMovingX")
	@Transition(state = "wallDetected", msgId = SystemData.endMoveOkId)
	protected void robotMovingX(IApplMessage msg) {
		if (turnDir) {
			addTransition("endWork", SystemData.endMoveKoId);
		}else {
			addTransition("wallDetected",SystemData.endMoveKoId);
		}
		VRobotMoves.step(getName(), conn);
		x++;
		contPassi++;
		currentMove = "step";
	}


	@State(name = "continueWork")
	@Transition(state = "robotMovingY", msgId = SystemData.endMoveOkId)
	@Transition(state = "wallDetected", msgId = SystemData.endMoveKoId)
	protected void continueWork(IApplMessage msg) {
		outInfo("" + msg);
		// JSONObject move = new JSONObject( msg.msgContent());
		if (msg.msgContent().equals("turnLeft")) {
			ncorner++;
// 			VRobotMoves.step(getName(),conn);
// 			currentMove="step"; 
		}
		VRobotMoves.step(getName(), conn);
		currentMove = "step";
	}

	/*
	 * Transizioni condizionate (con guardie)
	 */
	@State(name = "wallDetected")
	protected void wallDetected(IApplMessage msg) {
		if (turnDir) {
			currentMove = "turnLeft";
			VRobotMoves.turnLeft(getName(), conn);
			rotation++;
			if (rotation == 1)
				addTransition("robotMovingX", SystemData.endMoveOkId);
			else {
				rotation = 0;
				turnDir = !turnDir;
				addTransition("robotMovingY", SystemData.endMoveOkId);
			}

		} else {
			currentMove = "turnRight";
			VRobotMoves.turnRight(getName(), conn);
			rotation++;
			if (rotation == 1)
				addTransition("robotMovingX", SystemData.endMoveOkId);
			else {
				rotation = 0;
				turnDir = !turnDir;
				addTransition("robotMovingY", SystemData.endMoveOkId);
			}
		}
	}

	@State(name = "endWork")
	protected void endWork(IApplMessage msg) {
		System.out.println("CONTPASSI: "+contPassi+" AREA: "+y);
		if (y== contPassi) {
			outInfo("walk all the room");
		} else {
			outInfo("ERROR");
		}
		outInfo("BYE");
		System.exit(0);
	}

}
