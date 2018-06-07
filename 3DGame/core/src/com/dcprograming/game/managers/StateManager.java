
package com.dcprograming.game.managers;

import com.dcprograming.game.states.MenuState;
import com.dcprograming.game.states.NetworkGameState;
import com.dcprograming.game.states.State;

/**
 * Manages the updating, rendering, disposing, and switching of states.
 * 
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted May 24, 2018
 * @version 1.05
 */
public class StateManager {

	public static final int MENU = 1, GAME = 2;

	private State state;

	/**
	 * Sets the starting state
	 * 
	 * @param startingState
	 */
	public StateManager(int startingState) {
		setState(startingState);
	}

	/**
	 * Sets state based on defined ints corresponding to state types
	 * 
	 * @param newState - the new state type
	 */
	public void setState(int newState) {

		if (state != null)
			state.dispose();
		switch (newState) {
		case MENU:
			state = new MenuState(this);
			break;
		case GAME:
			state = new NetworkGameState(this, "", false);
			break;
		}
	}

	/**
	 * Sets state based on an existing built state
	 * 
	 * @param state - the pre-built state
	 */
	public void setState(State state) {
		if (this.state != null)
			this.state.dispose();
		this.state = state;
	}

	/**
	 * Updates the current state.
	 * 
	 * @param deltaTime - the time between updates
	 */
	public void update(float deltaTime) {

		state.update(deltaTime);
	}

	/**
	 * Renders the current state
	 */
	public void render() {

		state.render();
	}

	/**
	 * Clears the resources of the current state
	 */
	public void dispose() {

		state.dispose();
	}
}
