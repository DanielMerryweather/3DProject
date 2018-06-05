/**
 * @author Colton Giesbrecht
 * @dateCreated May 22, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.collision.BoundingBox;

public class PhysicPlayer extends Player {

	public BoundingBox bounds;
	private float ychange;

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param fov
	 * @param ilx
	 * @param ily
	 * @param ilz
	 */
	public PhysicPlayer(float x, float y, float z, float fov, float ilx, float ily, float ilz, Color teamColour) {
		super(x, y, z, fov, ilx, ily, ilz, teamColour);
		// Vector3 max = new Vector3(playerPosition.x + 0.3f, playerPosition.y,
		// playerPosition.z + 0.3f);
		// Vector3 min = new Vector3(playerPosition.x - 0.3f, playerPosition.y - height,
		// playerPosition.z - 0.3f);
		// bounds = new BoundingBox(min, max);
	}

	public void localTranslate(float cx, float cy, float cz, float dt, ArrayList<Entity> entities) {

		localTranslate(cx, 0, 0, dt);
		for (int i = 1; i < entities.size(); i++) {
			if (entities.get(i).intercept(this)) {
				do {
					localTranslate(-cx * 0.0001f, 0, 0, dt);

				} while (entities.get(i).intercept(this));
			}
		}
		localTranslate(0, cy, 0, dt);
		for (int i = 1; i < entities.size(); i++) {
			if (entities.get(i).intercept(this)) {
				do {
					localTranslate(0, -cy * 0.0001f, 0, dt);

				} while (entities.get(i).intercept(this));
			}
		}
		localTranslate(0, 0, cz, dt);
		for (int i = 1; i < entities.size(); i++) {
			if (entities.get(i).intercept(this)) {
				do {
					localTranslate(0, 0, -cz * 0.0001f, dt);

				} while (entities.get(i).intercept(this));
			}
		}
	}

	public void update(float dt, float gravity) {

		super.update(dt);
		// if (ychange != 0)
		// ychange -= gravity * dt;
	}
}
