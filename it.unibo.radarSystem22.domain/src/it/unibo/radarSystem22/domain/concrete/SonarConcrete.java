package it.unibo.radarSystem22.domain.concrete;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.models.SonarModel;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class SonarConcrete extends SonarModel implements ISonar {
	private Process process ;
	private BufferedReader reader ;
	
	protected void sonarSetUp() {
		currentValue = new Distance(90);
	}

	protected void sonarProduce() {
		try {
		    String data = reader.readLine(); //lettura da processo che esegue il file c
		    if( data == null ) return;
		    int value = Integer.parseInt(data);
		    int lastSonarVal = currentValue.getVal();
		    
		    if( lastSonarVal != value && value < DomainSystemConfig.sonarDistanceMax) {
		    	currentValue=new Distance(value);
		    }
		  }catch( Exception e) { }

	}

	public void activate() {
		if( process == null ) {
			  try {
			    process=Runtime.getRuntime().exec("sudo ./SonarAlone"); //file c eseguito di funzionamento sonar
			    reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
			  }catch( Exception e) { }
		}		
	}

	public void deactivate() {
		currentValue= new Distance(90);
		if( process != null ) {
		    process.destroy();
		    process=null;
		}
	}


}
