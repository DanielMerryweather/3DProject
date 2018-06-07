/**
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.managers;

import com.dcprograming.game.states.MenuState;
import com.dcprograming.game.states.NetworkGameState;
import com.dcprograming.game.states.State;

public class StateManager {

	public static final int MENU = 1, GAME = 2, GAME_OVER = 3, NETWORKTEST = 4;

	public State state;

	public StateManager(int startingState) {
		setState(startingState);
	}

	public void setState(int newState) {

		if (state != null)
			state.dispose();
		switch (newState) {
		case MENU:
			state = new MenuState(this);
			break;
		case GAME:
			// state = new GameState(this);
			break;
		case NETWORKTEST:
			state = new NetworkGameState(this, "", false);
			break;
		}
	}

	public void setState(State state) {
		if (this.state != null)
			this.state.dispose();
		this.state = state;
	}

	public void update(float deltaTime) {

		state.update(deltaTime);
	}

	public void render() {

		state.render();
	}

	public void dispose() {

		state.dispose();
	}
}
