package com.dcprograming.game.core;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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

	//PerspectiveCamera cam;
	ModelBatch models;
	Model sphere;
	Model plane;
	Model coords;
	ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();
	Environment world;
	
	float grav = -9.81f;
	float y = 0f;
	
	Player p;
	
	ModelBuilder mb = new ModelBuilder();
	
	DirectionalShadowLight sl;
	ModelBatch sb;
	
	@SuppressWarnings("deprecation")
	@Override
	public void create() {
		p = new Player(0, 1, -3, 110, 0, 0, 0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		sphere = mb.createSphere(1f, 1f, 1f, 16, 16, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Normal | Usage.Position);
		plane = mb.createBox(3f, 0.1f, 3f, new Material(ColorAttribute.createSpecular(Color.WHITE)), Usage.Normal | Usage.Position);
		instances.add(new ModelInstance(sphere, 0, 2f, 0));
		instances.add(new ModelInstance(mb.createRect(20f, 0f, 20f, 20f, 0f, -20f, -20f, 0f, -20f, -20f, 0f, 20f, 0f, 1f, 0f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Normal | Usage.Position)));
		//instances.add(new ModelInstance(plane));
		/*for(int x=-5;x<5;x++) {
			for(int y=-5;y<5;y++) {
				instances.add(new ModelInstance(plane, x*3f, Math.abs(x*x+y*y)*0.1f, y*3f));
			}
		}*/
		for(int i=0;i<15;i++) {
			instances.add(new ModelInstance(mb.createBox(2.5f, 2.5f, 2.5f, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), Usage.Normal | Usage.Position), (float)(Math.random()*2-1)*20f, 1.25f, (float)(Math.random()*2-1)*20f));
		}
		models = new ModelBatch();
		world = new Environment();
		world.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1));
		world.add((sl = new DirectionalShadowLight(4096, 4096, 10f, 10f, .1f, 100f)).set(1f, 1f, 1f, new Vector3(-1f,-1f,-1f)));
		world.shadowMap = sl;
		
		coords = mb.createXYZCoordinates(1f, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Normal | Usage.Position);
		instances.add(new ModelInstance(coords, 0, 1f, 0));
		
		sb = new ModelBatch(new DepthShaderProvider());
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
		
		sl.begin(p.playerPosition, p.playerCam.direction);
		
		sb.begin(sl.getCamera());
		sb.render(instances);
		sb.end();
		
		sl.end();
		
		Gdx.gl.glClearColor(0.2f, 0.7f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		instances.get(0).transform.translate(0, y, 0);
		if(instances.get(0).transform.getTranslation(new Vector3()).y <= 0.5f){
			y = -y*1f;
			y -= 9.81*(dtime/20);
			instances.get(0).transform.setTranslation(new Vector3(0, 0.5f, 0));
		}else{
			y -= 9.81*(dtime/20);
		}
		
		if(Gdx.input.isCursorCatched()){
			p.rotate(0.3f);
			float cx =( Gdx.input.isKeyPressed(Keys.A)?1:0) + (Gdx.input.isKeyPressed(Keys.D)?-1:0);
			float cy =( Gdx.input.isKeyPressed(Keys.SPACE)?1:0) + (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)?-1:0);
			float cz =( Gdx.input.isKeyPressed(Keys.W)?1:0) + (Gdx.input.isKeyPressed(Keys.S)?-1:0);
			float speed = 0.05f;
			p.localTranslate(cx*speed, cy*speed, cz*speed);
		}
		
		p.logInfo();
		
		models.begin(p.playerCam);
		models.render(instances, world);
		models.end();
	}
	
	@Override
	public void dispose() {

	}
}