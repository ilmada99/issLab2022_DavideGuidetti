package unibo.actor22.common;

import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import unibo.actor22.*;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

public class ControllerForActor extends QakActor22 {
	protected IRadarDisplay radar;
	// protected IApplMessage getStateRequest ; //Eliminato per osservazione Filoni
	protected boolean on = true;
	protected IApplMessage getStateRequest;

	public ControllerForActor(String name) {
		super(name);
		radar = DeviceFactory.createRadarGui();
		getStateRequest = Qak22Util.buildRequest(name,"ask", ApplData.reqLedState,ApplData.ledName);
	}

	@Override
	protected void handleMsg(IApplMessage msg) {
		if (msg.isEvent())
			elabEvent(msg);
		else if (msg.isDispatch())
			elabCmd(msg);
		else if (msg.isReply())
			elabReply(msg);
	}

	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		ColorsOut.outappl(getName() + " | elabCmd=" + msgCmd + " obs=" + RadarSystemConfig.sonarObservable,
				ColorsOut.BLUE);
		if (msgCmd.equals(ApplData.cmdActivate)) {
			sendMsg(ApplData.activateSonar);
			doControllerWork();
		}
	}

	protected void elabReply(IApplMessage msg) {
		ColorsOut.outappl(getName() + " | elabReply=" + msg, ColorsOut.GREEN);
		// if( msg.msgId().equals(ApplData.reqDistance ))
		String dStr = msg.msgContent();
		int d = Integer.parseInt(dStr);
		// Radar
		radar.update(dStr, "60");
		// LedUse case
		if (d < RadarSystemConfig.DLIMIT) {
			forward(ApplData.turnOffLed);
			forward(ApplData.deactivateSonar);
		} else {
			// forward(ApplData.turnOffLed);
			//request(getStateRequest);
			forward(ApplData.turnOnLed);
			doControllerWork();
		}
	}

	protected void elabEvent(IApplMessage msg) {
		ColorsOut.outappl(getName() + " | elabEvent=" + msg, ColorsOut.GREEN);
		if (msg.isEvent()) { // defensive
			String dstr = msg.msgContent();
			int d = Integer.parseInt(dstr);
			radar.update(dstr, "60");
			if (d < RadarSystemConfig.DLIMIT) {
				// forward(ApplData.turnOnLed);
				forward(ApplData.deactivateSonar);
			} else {
				// forward(ApplData.turnOffLed);
			}
		}
	}

	protected void doControllerWork() {
		CommUtils.aboutThreads(getName() + " |  Before doControllerWork on=" + on);
		CommUtils.delay(100);
		request(ApplData.askDistance);

	}

	protected void elabAnswer(IApplMessage msg) {
		CommUtils.delay(500);
		System.out.println("messaggio: "+msg.msgContent());
		doControllerWork();
	}

}
