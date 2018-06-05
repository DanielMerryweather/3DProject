/**
 * @author Colton Giesbrecht
 * @dateCreated May 22, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.entities;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.dcprograming.game.managers.CullingModelBatch;

public class Entity {

	protected BoundingBox bounds = new BoundingBox();
	protected ModelInstance model;
	protected static ModelBuilder builder = new ModelBuilder();
	protected float x, y, z;

	/**
	 * @param model
	 * @param x
	 * @param y
	 * @param z
	 */
	public Entity(float x, float y, float z) {

		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void update(float deltaTime) {

		if (bounds.getWidth() == 0)
			model.calculateBoundingBox(bounds);
		bounds.set(new Vector3(x - bounds.getWidth() / 2, y - bounds.getHeight() / 2, z - bounds.getDepth() / 2),
				new Vector3(x + bounds.getWidth() / 2, y + bounds.getHeight() / 2, z + bounds.getDepth() / 2));
	}

	public void render(CullingModelBatch renderer) {

		renderer.render(model);
	}

	public void render(CullingModelBatch renderer, Environment world) {

		renderer.render(model, world);
	}

	public boolean intercept(Entity otherEntity) {

		return bounds.intersects(otherEntity.bounds);
	}
}
