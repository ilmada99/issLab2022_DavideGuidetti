package unibo.actor22.distrib;

import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22.annotations.ActorRemote;
import unibo.actor22.common.ApplData;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.context.EnablerContextForActors;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@ActorLocal(name = { ApplData.controllerName, ApplData.radarName }, implement = {
		unibo.actor22.common.ControllerObservableActor.class, unibo.actor22.common.RadarActor.class })
@ActorRemote(name = { ApplData.ledName, ApplData.sonarName },
		host = { "localhost", "localhost" }, 
		port = { "" + ApplData.ctxPort, "" + ApplData.ctxPort }, protocol = { "TCP", "TCP" })

public class RSActor22DistSonarObsOnPC {
	private EnablerContextForActors ctx;

	public void doJob() {
		ColorsOut.outappl(this.getClass().getName() + " | Start", ColorsOut.BLUE);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		execute();
		terminate();
	}

	protected void configure() {
		DomainSystemConfig.tracing = false;
		CommSystemConfig.protcolType = ProtocolType.tcp;
		CommSystemConfig.tracing = false;
		DomainSystemConfig.DLIMIT = 95;
		ProtocolType protocol = CommSystemConfig.protcolType;

		ctx = new EnablerContextForActors("ctx", ApplData.ctxPort, ApplData.protocol);

		Qak22Context.handleLocalActorDecl(this);
		Qak22Context.handleRemoteActorDecl(this);
	}

	protected void execute() {
		ColorsOut.outappl(this.getClass().getName() + " | execute", ColorsOut.MAGENTA);
		Qak22Util.sendAMsg(ApplData.activateCrtl);
		ctx.activate();
	}

	public void terminate() {
		CommUtils.delay(10000);
		System.exit(0);
	}

	public static void main(String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new RSActor22DistSonarObsOnPC().doJob();
		CommUtils.aboutThreads("Before end - ");
	}
}
