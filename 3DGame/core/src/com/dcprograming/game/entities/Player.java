package com.dcprograming.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Player extends Entity {

	public PerspectiveCamera playerCam;
	public Color teamColour;

	int height = 1;

	Quaternion playerRotation;
	public Vector3 playerPosition;

	public Player(float x, float y, float z, float fov, float ilx, float ily, float ilz, Color teamColour) {

		super(x, y, z);
		this.teamColour = teamColour;
		playerCam = new PerspectiveCamera(fov, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
		playerCam.near = .1f;
		playerCam.far = 300f;
		playerPosition = new Vector3(x, y, z);
		playerCam.translate(x, y + height, z);
		playerCam.lookAt(new Vector3(ilx, ily, ilz));
		playerCam.update();
		model = new ModelInstance(
				builder.createBox(0.5f, height, 0.5f, new Material(IntAttribute.createCullFace(GL20.GL_NONE), ColorAttribute.createDiffuse(teamColour)), Usage.Normal | Usage.Position), x, y, z);

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
		model.transform.translate(nx, ny, nz);

		Vector3 position = playerCam.position;
		x = position.x;
		y = position.y;
		z = position.z;
		super.update(dt);
	}

	public void logInfo() {
		System.out.println("X: " + playerPosition.x + ", \tY: " + playerPosition.y + ", \tZ: " + playerPosition.z);
	}

}
