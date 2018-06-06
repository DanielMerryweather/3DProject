package com.dcprograming.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * @author 50018003
 * By: Daniel Merryweather
 * The Player class handles all the first person player operations, such as movement and mouse based rotation
 * @dateCreated May 24, 2018
 * @dateCompleted June 5, 2018
 * @version 1.3
 */
public class Player {

	public PerspectiveCamera playerCam;

	int height = 1;

	public float pitch = 90;
	public float yaw = 0;
	public Vector3 playerPosition;

	/**
	 * Constructor for player initialization
	 * @param x - X position
	 * @param y - Y position
	 * @param z - Z position
	 * @param fov - Field of View
	 * @param ilx - Initial look X position
	 * @param ily - Initial look Y position
	 * @param ilz - Initial look Z position
	 */
	public Player(float x, float y, float z, float fov, float ilx, float ily, float ilz) {

		playerCam = new PerspectiveCamera(fov, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
		playerCam.near = .1f;
		playerCam.far = 30f;
		playerPosition = new Vector3(x, y, z);
		playerCam.translate(x, y + height, z);
		playerCam.lookAt(new Vector3(ilx, ily, ilz));
		playerCam.update();
		
	}

	/**
	 * Rotation based on mouse movement
	 * @param sens - Mouse sensitivity
	 */
	public void rotate(float sens) {
		playerCam.rotate(Vector3.Y, -sens * Gdx.input.getDeltaX());
		playerCam.rotate(playerCam.direction.cpy().crs(Vector3.Y), -sens * Gdx.input.getDeltaY());
		
		pitch = (float) (Math.acos(Math.sqrt(playerCam.up.x * playerCam.up.x + playerCam.up.z * playerCam.up.z)) * (playerCam.direction.y > 0 ? 1:-1) / Math.PI * 180 + (playerCam.direction.y > 0 ? 0:180));
		yaw = (float) (Math.acos(playerCam.up.z / Math.sqrt(playerCam.up.x * playerCam.up.x + playerCam.up.z * playerCam.up.z)) * (-Math.abs(playerCam.up.x) / playerCam.up.x) / Math.PI * 180
				+ (playerCam.direction.y > 0 ? -180 : 0));
		if (Float.isNaN(yaw)) {
			yaw = 0;
		}

		while(playerCam.up.y < 0) {
			if(Math.abs(pitch) < 90) {
				playerCam.rotate(playerCam.direction.cpy().crs(Vector3.Y), 1);
			}else {
				playerCam.rotate(playerCam.direction.cpy().crs(Vector3.Y), -1);
			}
		}
		playerCam.update();
	}

	/**
	 * Sets the position of the player
	 * @param nx - new X
	 * @param ny - new Y
	 * @param nz - new Z
	 */
	public void setPosition(float nx, float ny, float nz) {

		playerCam.translate(nx, ny, nz);
		playerPosition.add(nx, ny, nz);
		playerCam.update();
	}

	/**
	 * Local translation for the player, relative to current rotation
	 * @param cx - Change in X
	 * @param cy - Change in Y
	 * @param cz - Change in Z
	 * @param dt - Deltatime / scale
	 */
	public void localTranslate(float cx, float cy, float cz, float dt) {
		float nx = dt * ((float) Math.sin(-yaw / 180 * Math.PI) * cz + (float) Math.sin((90 - yaw) / 180 * Math.PI) * cx);
		float ny = dt * cy;
		float nz = dt * ((float) Math.cos(-yaw / 180 * Math.PI) * cz + (float) Math.cos((90 - yaw) / 180 * Math.PI) * cx);
		setPosition(nx, ny, nz);
	}

	/**
	 * Called to display current information about this player
	 */
	public void logInfo() {
		System.out.println("X: " + playerPosition.x + ", \tY: " + playerPosition.y + ", \tZ: " + playerPosition.z);
	}

}
