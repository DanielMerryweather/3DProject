package com.dcprograming.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Player {

	public PerspectiveCamera playerCam;

	int height = 1;

	Quaternion playerRotation;
	public Vector3 playerPosition;

	public Player(float x, float y, float z, float fov, float ilx, float ily, float ilz) {

		playerCam = new PerspectiveCamera(fov, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
		playerCam.near = .1f;
		playerCam.far = 30f;
		playerPosition = new Vector3(x, y, z);
		playerCam.translate(x, y + height, z);
		playerCam.lookAt(new Vector3(ilx, ily, ilz));
		playerCam.update();

		playerRotation = playerCam.view.getRotation(new Quaternion());
	}

	public void rotate(float sens) {
		playerCam.rotate(Vector3.Y, -sens * Gdx.input.getDeltaX());
		playerCam.rotate(playerCam.direction.cpy().crs(Vector3.Y), -sens * Gdx.input.getDeltaY());
		playerRotation = playerCam.view.getRotation(new Quaternion());
		playerCam.update();
	}

	public void localTranslate(float cx, float cy, float cz, float dt) {
		float nx = dt * ((float) Math.sin(-playerRotation.getYaw() / 180 * Math.PI) * -cz + (float) Math.sin((90 - playerRotation.getYaw()) / 180 * Math.PI) * -cx);
		float ny = dt * (float) Math.cos(-playerRotation.getPitch() / 180 * Math.PI) * cy;
		float nz = dt * ((float) Math.cos(-playerRotation.getYaw() / 180 * Math.PI) * -cz + (float) Math.cos((90 - playerRotation.getYaw()) / 180 * Math.PI) * -cx);
		playerCam.translate(nx, ny, nz);
		playerPosition.add(nx, ny, nz);
		playerCam.update();
	}

	public void logInfo() {
		System.out.println("X: " + playerPosition.x + ", \tY: " + playerPosition.y + ", \tZ: " + playerPosition.z);
	}

}
