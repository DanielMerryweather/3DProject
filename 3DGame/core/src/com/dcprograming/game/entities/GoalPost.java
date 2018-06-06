/**
 * @author Colton Giesbrecht
 * @dateCreated Jun 5, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class GoalPost extends Entity {

	private static final float THICKNESS = 0.5f;
	private static final float HEIGHT = 2;

	public GoalPost(float x, float y, float z) {
		super(x, y + HEIGHT / 2, z);
		model = new ModelInstance(builder.createBox(THICKNESS, HEIGHT, THICKNESS, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.Normal), x, y + HEIGHT / 2, z);
	}

}
