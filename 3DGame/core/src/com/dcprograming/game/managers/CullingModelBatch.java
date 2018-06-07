
package com.dcprograming.game.managers;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Creates a modelbatch that culls entities out of the player's frustum.
 * 
 * @author Colton Giesbrecht
 * @dateCreated May 23, 2018
 * @dateCompleted May 23, 2018
 * @version 1.00
 */
public class CullingModelBatch extends ModelBatch {

	private ObjectMap<Mesh, Float> radiuses = new ObjectMap<Mesh, Float>();

	private Vector3 tmp = new Vector3();

	/**
	 * Provides depthShaderProvider to the modelbatch constructor
	 * 
	 * @param depthShaderProvider
	 */
	public CullingModelBatch(DepthShaderProvider depthShaderProvider) {
		super(depthShaderProvider);
	}

	/**
	 * Default empty constructor
	 */
	public CullingModelBatch() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * For each mesh being rendered, check if they are in the player's camera
	 * frustum and do not render if they are not in view
	 */
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

	/**
	 * Takes a model mesh and returns the radius of the mesh
	 * 
	 * @param mesh
	 * @return radius
	 */
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