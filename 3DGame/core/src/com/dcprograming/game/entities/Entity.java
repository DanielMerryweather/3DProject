
package com.dcprograming.game.entities;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.dcprograming.game.managers.CullingModelBatch;

/**
 * Gives rendering, updating, and collision abilities to entities
 * 
 * @author Colton Giesbrecht
 * @dateCreated May 22, 2018
 * @dateCompleted May 23, 2018
 * @version 1.00
 */
public class Entity {

	protected BoundingBox bounds = new BoundingBox();
	public ModelInstance model;
	protected static ModelBuilder builder = new ModelBuilder();
	public float x, y, z;

	/**
	 * Sets the entity's position and creates the model object
	 * 
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

	/**
	 * Updates the bounding box for the entity
	 * 
	 * @param deltaTime - time since last update
	 */
	public void update(float deltaTime) {

		if (bounds.getWidth() == 0)
			model.calculateBoundingBox(bounds);
		bounds.set(new Vector3(x - bounds.getWidth() / 2, y - bounds.getHeight() / 2, z - bounds.getDepth() / 2),
				new Vector3(x + bounds.getWidth() / 2, y + bounds.getHeight() / 2, z + bounds.getDepth() / 2));
	}

	/**
	 * renders the object using a cullingmodelbatch
	 * 
	 * @param renderer - the cullingmodelbatch
	 */
	public void render(CullingModelBatch renderer) {

		renderer.render(model);
	}

	/**
	 * renders the object using a cullingmodelbatch in ref with the world enviroment
	 * 
	 * @param renderer - the cullingmodelbatch
	 * @param world - the world enviroment
	 */
	public void render(CullingModelBatch renderer, Environment world) {

		renderer.render(model, world);
	}

	/**
	 * Checks if another entity is inside it's bounding box
	 * 
	 * @param otherEntity
	 * @return isIntercepted
	 */
	public boolean intercept(Entity otherEntity) {

		return bounds.intersects(otherEntity.bounds);
	}
}
