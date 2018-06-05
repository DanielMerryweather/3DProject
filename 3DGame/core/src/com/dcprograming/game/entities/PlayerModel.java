package com.dcprograming.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class PlayerModel extends Entity {

	public static final float HEIGHT = 1;
	public static final float WIDTH = 1f;

	public Color teamColour;

	public PlayerModel(float x, float y, float z, Color teamColour, ModelBuilder mb) {
		super(x, y, z);
		this.teamColour = teamColour;
		model = new ModelInstance(mb.createBox(WIDTH, HEIGHT, WIDTH, new Material(ColorAttribute.createDiffuse(teamColour)), Usage.Position | Usage.Normal), x, y, z);
	}

	public void rotate(float pitch, float yaw) {
		// model.transform.rotate(axis, degrees);
	}

}
