package it.unibo.radarSystem22.domain.interfaces;

public interface IObservable {
	public void addObserver(ISonarObserver observer);
	public void removeObserver(ISonarObserver observer);
}
