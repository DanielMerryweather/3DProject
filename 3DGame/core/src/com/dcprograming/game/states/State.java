/**
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.states;

import com.dcprograming.game.managers.StateManager;

public abstract class State {

	protected StateManager stateManager;

	public State(StateManager stateManager) {

		this.stateManager = stateManager;
	}

	public abstract void render();

	public abstract void update(float deltaTime);

	public abstract void dispose();
}
