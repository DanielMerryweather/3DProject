package com.dcprograming.game.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Core extends ApplicationAdapter {

	PerspectiveCamera cam;
	ModelBatch mb;
	ModelBuilder builder;
	Model box;
	ModelInstance realBox;
	Vector3 boxMotion;
	ModelInstance realBox2;
	Vector3 boxMotion2;
	Environment world;

	@Override
	public void create() {

		cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.translate(0, 1, -15);
		cam.lookAt(new Vector3(0, 0, 0));
		cam.update();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		builder = new ModelBuilder();
		box = builder.createSphere(2f, 2f, 2f, 16, 16, new Material(ColorAttribute.createDiffuse(Color.BLUE), ColorAttribute.createSpecular(1, 1f, 0.9f, 0.1f)), Usage.Normal | Usage.Position);
		realBox = new ModelInstance(box, -6, 0, 0);
		realBox2 = new ModelInstance(box, 6, 0, 0);
		boxMotion = new Vector3(0.5f, 0, 0);
		boxMotion2 = new Vector3(0, 5, 0);
		mb = new ModelBatch();
		world = new Environment();
		world.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1));
		world.set(new ColorAttribute(ColorAttribute.Fog, 1f, 1f, 1f, 1));
		world.add(new DirectionalLight().set(Color.WHITE, new Vector3(1, -1, 1)));
	}

	@Override
	public void render() {

		float dtime = Gdx.graphics.getDeltaTime();
		boxMotion2.y -= 9.81f / 4;
		realBox.transform.translate(boxMotion.x * dtime, boxMotion.y * dtime, boxMotion.z * dtime);
		realBox2.transform.translate(boxMotion2.x * dtime, boxMotion2.y * dtime, boxMotion2.z * dtime);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		mb.begin(cam);
		mb.render(realBox, world);
		mb.render(realBox2, world);
		mb.end();
	}

	@Override
	public void dispose() {

	}
}
