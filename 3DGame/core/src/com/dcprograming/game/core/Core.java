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
	Environment world;

	@Override
	public void create() {

		cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.translate(0, 1, -5);
		cam.lookAt(new Vector3(0, 0, 0));
		cam.update();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		builder = new ModelBuilder();
		box = builder.createSphere(2f, 2f, 2f, 16, 16, new Material(ColorAttribute.createDiffuse(Color.BLUE), ColorAttribute.createSpecular(1, 1f, 0.9f, 0.1f)), Usage.Normal | Usage.Position);
		realBox = new ModelInstance(box, 0, 0, 0);
		mb = new ModelBatch();
		world = new Environment();
		world.set(new ColorAttribute(ColorAttribute.AmbientLight, 1, 0.1f, 0.1f, 1));
		world.set(new ColorAttribute(ColorAttribute.Fog, 1f, 1f, 1f, 1));
		world.add(new DirectionalLight().set(Color.WHITE, new Vector3(1, -1, 1)));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		cam.rotateAround(Vector3.Zero, new Vector3(0, 1, 0), 0.1f);
		cam.update();
		mb.begin(cam);
		mb.render(realBox, world);
		mb.end();
	}

	@Override
	public void dispose() {

	}
}
