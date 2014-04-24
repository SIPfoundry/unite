package org.ezuce.unitemedia.context;

import java.util.Observable;

import org.ezuce.unitemedia.event.UniteEvent;

public abstract class AbstractUser extends Observable {
	
	public void notify(UniteEvent event) {
		setChanged();
		notifyObservers(event);
	}

}
