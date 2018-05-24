/**
 * @author Colton Giesbrecht
 * @dateCreated May 22, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.entities;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Entity extends ModelInstance {

	public final BoundingBox bounds = new BoundingBox();
	private final Vector3 dimension = new Vector3();
	private final Vector3 center = new Vector3();
	private final float radius;

	/**
	 * @param model
	 * @param x
	 * @param y
	 * @param z
	 */
	public Entity(Model model, float x, float y, float z) {
		super(model, x, y, z);
		calculateBoundingBox(bounds);
		bounds.getDimensions(dimension);
		bounds.set(new Vector3(x - bounds.getWidth() / 2, y - bounds.getHeight() / 2, z - bounds.getDepth() / 2),
				new Vector3(x + bounds.getWidth() / 2, y + bounds.getHeight() / 2, z + bounds.getDepth() / 2));
		radius = dimension.len() / 2;
	}

	public boolean isVisible(PerspectiveCamera cam) {
		transform.getTranslation(dimension);
		dimension.add(center);
		return cam.frustum.sphereInFrustumWithoutNearFar(center, radius);
	}
}
