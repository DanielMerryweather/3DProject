/**
 * Builds a spherical ball entity with a static radius.
 * @author Colton Giesbrecht
 * @dateCreated May 27, 2018
 * @dateCompleted June 4, 2018
 * @version 1.50
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
	public String colour = "GREY";

	float grav = -9.81f;

	/**
	 * Builds a ball model and its bounding box. Sets colour to grey and sets up the
	 * movement vector.
	 * 
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

	/**
	 * Translates the ball and it's bounding box.
	 * 
	 * @param xc - the change in x
	 * @param yc - the change in y
	 * @param zc - the change in z
	 */
	private void move(float xc, float yc, float zc) {

		x += xc;
		y += yc;
		z += zc;
		model.transform.setTranslation(x, y, z);

	}

	/**
	 * Multiplies the movement vector by the parameter values.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void reflectDir(float x, float y, float z) {

		movement.scl(x, y, z);
	}

	/**
	 * Launches the ball in the direction that the player is facing if they are
	 * holding it and clicking
	 * 
	 * @param deltaTime - the time between updates
	 * @param pitch - the pitch of the player model
	 * @param yaw - the yaw of the player model
	 * @param launch - to launch or not
	 * @param held - is the ball held by a player
	 */
	public void update(float deltaTime, float pitch, float yaw, boolean launch, boolean held) {

		if (launch) {
			System.out.println(pitch + ", " + yaw);
			movement.set((float) (Math.sin(-yaw / 180 * Math.PI)), (float) (Math.cos(-pitch / 180 * Math.PI)), (float) (Math.cos(yaw / 180 * Math.PI)));
			movement.scl(2f);
			move(movement.x, movement.y, movement.z);
			movement.scl(5.5f);
		} else if (!held) {
			movement.y += grav * deltaTime;
			move(movement.x * deltaTime, movement.y * deltaTime, movement.z * deltaTime);
		}
		super.update(deltaTime);
	}

	/**
	 * changes the ball colour to a new colour
	 * 
	 * @param colour
	 */
	public void changeColor(Color colour) {

		model.materials.first().set(ColorAttribute.createDiffuse(colour));
	}
}