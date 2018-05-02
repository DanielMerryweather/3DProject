package com.daedalus.game;

import java.awt.Point;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.BSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Core extends ApplicationAdapter {
	
	PerspectiveCamera cam;
	ModelBatch mb;
	Model box;
	ModelBuilder builder;
	ModelInstance mi;
	Environment world;
	BoundingBox boxBound;
	
	@Override
	public void create () {
		cam = new PerspectiveCamera(20, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 5f, 20f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 300f;
		box = new Model();
		builder = new ModelBuilder();
		box = builder.createBox(2, 2, 2, new Material(ColorAttribute.createSpecular(Color.WHITE), ColorAttribute.createDiffuse(0,0,1,0.1f)), Usage.Normal | Usage.Position);
		mi = new ModelInstance(box, 0, 0, 0);
		mb = new ModelBatch();
		world = new Environment();
		world.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
		world.add(new DirectionalLight().set(Color.YELLOW, new Vector3(0f,-1,1)));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.9f, 0.9f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		cam.update();
		mi.transform.rotate(new Vector3(0,1,0), 0.3f);
		mb.begin(cam);
		mb.render(mi, world);
		mb.end();
	}
	
	@Override
	public void dispose () {

	}
}
