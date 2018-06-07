
package com.dcprograming.game.states;

import com.dcprograming.game.managers.StateManager;

/**
 * The abstract framework for any state used by the statemanager
 * 
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted May 24, 2018
 * @version 1.00
 */
public abstract class State {

	protected StateManager stateManager;

	/**
	 * Sets the controlling statemanager for later state switching
	 * 
	 * @param stateManager
	 */
	public State(StateManager stateManager) {

		this.stateManager = stateManager;
	}

	public abstract void render();

	public abstract void update(float deltaTime);

	public abstract void dispose();
}
