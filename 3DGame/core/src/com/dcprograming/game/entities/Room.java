/**
 * @author Colton Giesbrecht
 * @dateCreated Jun 3, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class Room extends Entity {

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Room(float x, float y, float z, float width, float height, float depth, Color wallColour, Color floorColour, boolean createCeiling) {
		super(x, y + height / 2, z);
		Material wallMaterial = new Material(ColorAttribute.createDiffuse(wallColour), ColorAttribute.createSpecular(Color.WHITE));
		Model ceiling = null;
		if (createCeiling) {

			ceiling = builder.createRect(width / 2, height, -depth / 2, width / 2, height, depth / 2, -width / 2, height, depth / 2, -width / 2, height, -depth / 2, 0, 1, 0, wallMaterial,
					Usage.Normal | Usage.Position);
		}
		Model floor = builder.createRect(width / 2, 0, depth / 2, width / 2, 0, -depth / 2, -width / 2, 0, -depth / 2, -width / 2, 0, depth / 2, 0, -1, 0,
				new Material(ColorAttribute.createDiffuse(floorColour), ColorAttribute.createSpecular(Color.WHITE)), Usage.Normal | Usage.Position);
		Model lWall = builder.createRect(width / 2, height, -depth / 2, width / 2, 0, -depth / 2, width / 2, 0, depth / 2, width / 2, height, depth / 2, -1, 0, 0, wallMaterial,
				Usage.Normal | Usage.Position);
		Model rWall = builder.createRect(-width / 2, 0, -depth / 2, -width / 2, height, -depth / 2, -width / 2, height, depth / 2, -width / 2, 0, depth / 2, 1, 0, 0, wallMaterial,
				Usage.Normal | Usage.Position);
		Model fWall = builder.createRect(-width / 2, 0, -depth / 2, width / 2, 0, -depth / 2, width / 2, height, -depth / 2, -width / 2, height, -depth / 2, 0, 0, 1, wallMaterial,
				Usage.Normal | Usage.Position);
		Model bWall = builder.createRect(width / 2, height, depth / 2, width / 2, 0, depth / 2, -width / 2, 0, depth / 2, -width / 2, height, depth / 2, 0, 0, -1, wallMaterial,
				Usage.Normal | Usage.Position);

		builder.begin();
		builder.node("floor", floor);
		builder.node("rWall", rWall);
		builder.node("lWall", lWall);
		builder.node("fWall", fWall);
		builder.node("bWall", bWall);
		if (createCeiling)
			builder.node("ceiling", ceiling);
		model = new ModelInstance(builder.end(), 0, 0, 0);
	}

	public boolean intercept(Entity otherEntity) {

		return !bounds.contains(otherEntity.bounds);
	}
}
