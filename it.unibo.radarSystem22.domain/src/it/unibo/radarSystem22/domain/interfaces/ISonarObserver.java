package it.unibo.radarSystem22.domain.interfaces;

public interface ISonarObserver {
	public void update(IDistance curVal); 
	public int getVal();
}
