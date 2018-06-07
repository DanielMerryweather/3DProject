
package com.dcprograming.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

/**
 * Builds a goalpost with a set size and given colour
 * 
 * @author Colton Giesbrecht
 * @dateCreated June 5, 2018
 * @dateCompleted June 5, 2018
 * @version 1.00
 */
public class GoalPost extends Entity {

	private static final float THICKNESS = 0.5f;
	private static final float HEIGHT = 2;

	/**
	 * Creates an entiety and sets its x, y, z values, builds the model and its
	 * bounding boxes.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param colour
	 */
	public GoalPost(float x, float y, float z, Color colour) {
		super(x, y + HEIGHT / 2, z);
		model = new ModelInstance(builder.createBox(THICKNESS, HEIGHT, THICKNESS, new Material(ColorAttribute.createDiffuse(colour)), Usage.Position | Usage.Normal), x, y + HEIGHT / 2, z);
	}

}
