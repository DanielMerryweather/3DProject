package com.dcprograming.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/**
 * @author 50018003
 * By: Daniel Merryweather
 * The Player Model class handles the rendered playermodel
 * @dateCreated May 24, 2018
 * @dateCompleted June 5, 2018
 * @version 1.0
 */
public class PlayerModel extends Entity {

	public static final float HEIGHT = 0.75f;
	public static final float WIDTH = 0.75f;

	public Color teamColour;

	/**
	 * Constructor for a new playermodel with color, position, and the current modelbuilder
	 * @param x - X position
	 * @param y - Y position
	 * @param z - Z position
	 * @param teamColour - Team Colour
	 * @param mb - Current modelbuilder
	 */
	public PlayerModel(float x, float y, float z, Color teamColour, ModelBuilder mb) {
		super(x, y, z);
		this.teamColour = teamColour;
		model = new ModelInstance(mb.createBox(WIDTH, HEIGHT, WIDTH, new Material(ColorAttribute.createDiffuse(teamColour)), Usage.Position | Usage.Normal), x, y, z);
	}

}
