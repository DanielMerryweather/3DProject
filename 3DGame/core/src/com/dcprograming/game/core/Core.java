/**
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.core;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.dcprograming.game.managers.StateManager;

public class Core extends ApplicationAdapter implements ComponentListener {

	private StateManager sm;

	public void create() {

		sm = new StateManager(StateManager.MENU);
	}

	public void render() {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.9f, 0.9f, 1, 1);

		sm.update(Gdx.graphics.getDeltaTime());
		sm.render();
	}

	public void dispose() {

	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
}
