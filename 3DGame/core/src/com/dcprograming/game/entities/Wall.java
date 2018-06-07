
package com.dcprograming.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

/**
 * Creates and manages a wall model and it's boundingbox
 * 
 * @author Colton Giesbrecht
 * @dateCreated May 29, 2018
 * @dateCompleted May 29, 2018
 * @version 1.00
 */
public class Wall extends Entity {

	/**
	 * Creates a wall using a rectangular prism model with y being equal to the
	 * bottom of the model and x and z being equal to one end
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Wall(float x, float y, float z, float xThickness, float yThickness, float zThickness, Color colour) {
		super(x + xThickness / 2, y + yThickness / 2, z + zThickness / 2);
		Model wall = builder.createBox(xThickness, yThickness, zThickness, new Material(ColorAttribute.createDiffuse(colour), ColorAttribute.createSpecular(Color.WHITE)),
				Usage.Position | Usage.Normal);
		model = new ModelInstance(wall, x + xThickness / 2, y + yThickness / 2, z + zThickness / 2);
	}

}