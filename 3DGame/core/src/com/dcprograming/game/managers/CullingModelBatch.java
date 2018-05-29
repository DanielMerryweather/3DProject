/**
 * @author Colton Giesbrecht
 * @dateCreated May 23, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.managers;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

public class CullingModelBatch extends ModelBatch {

	private ObjectMap<Mesh, Float> radiuses = new ObjectMap<Mesh, Float>();

	private Vector3 tmp = new Vector3();

	/**
	 * @param depthShaderProvider
	 */
	public CullingModelBatch(DepthShaderProvider depthShaderProvider) {
		super(depthShaderProvider);
	}

	/**
	 * 
	 */
	public CullingModelBatch() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void flush() {
		Iterator<Renderable> iter = renderables.iterator();
		while (iter.hasNext()) {
			Renderable renderable = iter.next();
			renderable.worldTransform.getTranslation(tmp);
			if (!camera.frustum.sphereInFrustumWithoutNearFar(tmp, getRadiusOfMesh(renderable.meshPart.mesh))) {
				iter.remove();
			}
		}
		super.flush();
	}

	private float getRadiusOfMesh(Mesh mesh) {
		Float radius = radiuses.get(mesh);
		if (radius != null) {
			return radius;
		}

		Vector3 dimensions = new Vector3();
		mesh.calculateBoundingBox().getDimensions(dimensions);
		radius = Math.max(Math.max(dimensions.x, dimensions.y), dimensions.z);

		radiuses.put(mesh, radius);
		return radius;
	}

}