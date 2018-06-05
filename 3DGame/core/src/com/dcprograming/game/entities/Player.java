package com.dcprograming.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class Player {

	public PerspectiveCamera playerCam;

	int height = 1;

	// Quaternion playerRotation;
	public float pitch = 90;
	public float yaw = 0;
	public Vector3 playerPosition;

	public Player(float x, float y, float z, float fov, float ilx, float ily, float ilz) {

		playerCam = new PerspectiveCamera(fov, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
		playerCam.near = .1f;
		playerCam.far = 30f;
		playerPosition = new Vector3(x, y, z);
		playerCam.translate(x, y + height, z);
		playerCam.lookAt(new Vector3(ilx, ily, ilz));
		playerCam.update();

		// playerRotation = playerCam.view.getRotation(new Quaternion());
	}

	public void rotate(float sens) {
		playerCam.rotate(Vector3.Y, -sens * Gdx.input.getDeltaX());
		playerCam.rotate(playerCam.direction.cpy().crs(Vector3.Y), -sens * Gdx.input.getDeltaY());
		// playerRotation = playerCam.view.getRotation(new Quaternion());
		pitch = (float) (Math.acos(Math.sqrt(playerCam.up.x * playerCam.up.x + playerCam.up.z * playerCam.up.z)) * (Math.abs(playerCam.up.y) / playerCam.up.y) / Math.PI * 180);
		yaw = (float) (Math.acos(playerCam.up.z / Math.sqrt(playerCam.up.x * playerCam.up.x + playerCam.up.z * playerCam.up.z)) * (-Math.abs(playerCam.up.x) / playerCam.up.x) / Math.PI * 180
				+ (playerCam.direction.y > 0 ? -180 : 0));
		if (Float.isNaN(yaw)) {
			yaw = 0;
		}

		if (pitch < 10) {
			playerCam.rotate(playerCam.direction.cpy().crs(Vector3.Y), sens * Gdx.input.getDeltaY());
		}
		if (pitch < 0) {
			playerCam.rotate(playerCam.direction.cpy().crs(Vector3.Y), (float) pitch * 2f);
		}
		playerCam.update();
	}

	public void setPosition(float nx, float ny, float nz) {

		playerCam.translate(nx, ny, nz);
		playerPosition.add(nx, ny, nz);
		playerCam.update();
	}

	public void localTranslate(float cx, float cy, float cz, float dt) {
		float nx = dt * ((float) Math.sin(-yaw / 180 * Math.PI) * cz + (float) Math.sin((90 - yaw) / 180 * Math.PI) * cx);
		float ny = dt * cy;
		float nz = dt * ((float) Math.cos(-yaw / 180 * Math.PI) * cz + (float) Math.cos((90 - yaw) / 180 * Math.PI) * cx);
		setPosition(nx, ny, nz);
	}

	public void logInfo() {
		System.out.println("X: " + playerPosition.x + ", \tY: " + playerPosition.y + ", \tZ: " + playerPosition.z);
	}

}
