/**
 * @author Colton Giesbrecht
 * @dateCreated May 27, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.math.Vector3;

public class Ball extends Entity {

	private static final float RADIUS = .3f;

	private Vector3 movement;
	private int heldPlayer = -1;

	/**
	 * @param model
	 * @param x
	 * @param y
	 * @param z
	 */
	public Ball(float x, float y, float z) {

		super(x, y + RADIUS, z);
		Model sphere = builder.createSphere(RADIUS, RADIUS, RADIUS, 20, 20,
				new Material(ColorAttribute.createDiffuse(Color.GRAY), ColorAttribute.createSpecular(Color.WHITE), IntAttribute.createCullFace(GL20.GL_NONE)), Usage.Normal | Usage.Position);
		model = new ModelInstance(sphere, x, y + RADIUS, z);
		movement = new Vector3(0, 0, 0);
	}

	private void move(float xc, float yc, float zc) {

		x += xc;
		y += yc;
		z += zc;
		model.transform.setTranslation(x, y, z);

	}

	public void reflectDir(float x, float y, float z) {

		movement.scl(x, y, z);
	}

	public void update(float deltaTime, float pitch, float yaw, boolean launch) {

		// if (heldPlayer < 0)
		// for (int i = 0; i < players.size(); i++)
		// if (player.intercept(this)) {
		// heldPlayer = i;
		// break;
		// }
		// if (heldPlayer >= 0) {
		// PlayerModel player = players.get(heldPlayer);
		// // x = (float) (player.x + RADIUS * 0.9f * (float)
		// // Math.cos(-player.playerRotation.getYaw() / 180 * Math.PI));
		// y = player.y - player.HEIGHT * 0.3f;
		// x = player.x;
		// z = player.z;
		// model.transform.setTranslation(x, y, z);
		// changeColor(player.teamColour);
		// // z = player.z + (float) (player.z + RADIUS * 0.9f *
		// // Math.sin(-player.playerRotation.getYaw() / 180 * Math.PI));

		if (launch) {
			System.out.println(pitch + ", " + yaw);
			movement.set((float) (Math.sin(-yaw / 180 * Math.PI)), (float) (Math.cos(-pitch / 180 * Math.PI)), (float) (Math.cos(yaw / 180 * Math.PI)));
			movement.scl(1f);
			move(movement.x, movement.y, movement.z);
			movement.scl(5.5f);
		} else
			move(movement.x * deltaTime, movement.y * deltaTime, movement.z * deltaTime);
		super.update(deltaTime);
	}

	public void changeColor(Color colour) {

		model.materials.first().set(ColorAttribute.createDiffuse(colour));
	}
}