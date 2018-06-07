/**
 * Runs a lwjglApplication using libgdx
 * 
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted May 24, 2018
 * @version 1.00
 */
package com.dcprograming.game.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.dcprograming.game.managers.StateManager;

public class Core extends ApplicationAdapter {

	private StateManager sm;
	public static int pixels;

	/**
	 * Sets the screen pixel measurement at Core creation
	 * 
	 * @param pixels
	 */
	public Core(int pixels) {
		Core.pixels = pixels;
	}

	/**
	 * Sets up the statemanager
	 */
	public void create() {

		sm = new StateManager(StateManager.MENU);
	}

	/**
	 * The render loop is called by the application repeatedly. Clears the graphics
	 * buffers and then calls the statemanager to render the current state.
	 */
	public void render() {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.9f, 0.9f, 1, 1);

		float deltaTime = Gdx.graphics.getDeltaTime();
		if (deltaTime > 1)
			deltaTime = 0;
		sm.update(deltaTime);
		sm.render();
	}

	/**
	 * Disposes of state resources.
	 */
	public void dispose() {
		sm.dispose();
	}
}
