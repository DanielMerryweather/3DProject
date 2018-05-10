package com.dcprograming.game.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Core extends ApplicationAdapter {

	PerspectiveCamera cam;
	ModelBatch spheres;
	Model sphere;
	Model plane;
	ModelInstance sph;
	ModelInstance pln;
	Environment world;
	
	float y = 0f;
	
	ModelBuilder mb = new ModelBuilder();
	
	Vector3 camRotation = new Vector3(0,0,0);
	
	DirectionalShadowLight sl;
	ModelBatch sb;
	
	@SuppressWarnings("deprecation")
	@Override
	public void create() {

		cam = new PerspectiveCamera(110, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.near = 0.1f;
		cam.far = 50f;
		cam.translate(0, 1, -3);
		cam.lookAt(new Vector3(0, 0, 0));
		cam.update();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		sphere = mb.createSphere(1f, 1f, 1f, 16, 16, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Normal | Usage.Position);
		plane = mb.createBox(3f, 0.1f, 3f, new Material(ColorAttribute.createDiffuse(Color.GRAY)), Usage.Normal | Usage.Position);
		sph = new ModelInstance(sphere, 0, 2f, 0);
		pln = new ModelInstance(plane);
		spheres = new ModelBatch();
		world = new Environment();
		world.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1));
		world.add((sl = new DirectionalShadowLight(1024, 1024, 15f, 15f, .1f, 50f)).set(1f, 1f, 1f, new Vector3(-1f,-1f,-1f)));
		world.shadowMap = sl;
		
		sb = new ModelBatch(new DepthShaderProvider());
		//world.add(new DirectionalLight().set(Color.WHITE, new Vector3(1, -1, 1)));
	}

	@Override
	public void render() {

		if(Gdx.input.isTouched()){
			Gdx.input.setCursorCatched(true);
		}
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Gdx.input.setCursorCatched(false);
		}
		
		float dtime = Gdx.graphics.getDeltaTime();
		
		sl.begin(Vector3.Zero, cam.direction);
		sb.begin(sl.getCamera());
		sb.render(sph);
		
		sb.end();
		sl.end();
		
		Gdx.gl.glClearColor(0.2f, 0.7f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		sph.transform.translate(0, y, 0);
		if(sph.transform.getTranslation(new Vector3()).y <= 0.5f){
			y = -y*1f;
			sph.transform.setTranslation(new Vector3(0, 0.5f, 0));
		}else{
			y -= 9.81*(dtime/20);
		}
		
		if(Gdx.input.isCursorCatched()){
			cam.rotate(Vector3.Y, -0.3f * Gdx.input.getDeltaX());
			cam.rotate(cam.direction.cpy().crs(Vector3.Y), -0.3f * Gdx.input.getDeltaY());
			cam.update();
		}
		
		spheres.begin(cam);
		spheres.render(sph, world);
		spheres.render(pln, world);
		spheres.end();
	}

	@Override
	public void dispose() {

	}
}