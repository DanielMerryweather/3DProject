/**
 * @author Colton Giesbrecht
 * @dateCreated May 22, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.core;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class PhysicPlayer extends Player {

	BoundingBox bounds;
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
	public PhysicPlayer(float x, float y, float z, float fov, float ilx, float ily, float ilz) {
		super(x, y, z, fov, ilx, ily, ilz);
		Vector3 max = new Vector3(playerPosition.x + 0.3f, playerPosition.y + height, playerPosition.z + 0.3f);
		Vector3 min = new Vector3(playerPosition.x - 0.3f, playerPosition.y, playerPosition.z - 0.3f);
		bounds = new BoundingBox(min, max);
	}

	public void localTranslate(float cx, float cy, float cz, float dt, ArrayList<Entity> instances) {

		if (cy != 0 && ychange == 0)
			ychange = 5;
		cy = ychange;
		localTranslate(cx, cy, cz, dt);
		Vector3 max = new Vector3(playerPosition.x + 0.3f, playerPosition.y + height, playerPosition.z + 0.3f);
		Vector3 min = new Vector3(playerPosition.x - 0.3f, playerPosition.y, playerPosition.z - 0.3f);
		bounds.set(min, max);
		boolean isCollided = false;
		for (int i = 1; i < instances.size(); i++)
			if (isCollidedBox(instances.get(i).bounds)) {
				isCollided = true;
				if (i == 1 && ychange != 0)
					ychange = 0;
			}
		if (isCollided)
			localTranslate(-cx, -cy, -cz, dt);
	}

	public boolean isCollidedBox(BoundingBox box) {

		return bounds.intersects(box);
	}

	public void update(float dt, float gravity) {

		if (ychange != 0)
			ychange -= gravity * dt;
	}
}
