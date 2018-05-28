/**
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.states;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.dcprograming.game.entities.Entity;
import com.dcprograming.game.entities.PhysicPlayer;
import com.dcprograming.game.managers.CullingModelBatch;
import com.dcprograming.game.managers.StateManager;

public class GameState extends State {
	
	
	CullingModelBatch models;
	Model sphere;
	Model plane;
	Model coords;
	ArrayList<Entity> instances = new ArrayList<Entity>();
	ArrayList<btCollisionObject> collisionShapes = new ArrayList<btCollisionObject>();
	Environment world;

	float grav = -9.81f;
	float y = 0f;

	PhysicPlayer p;

	ModelBuilder mb = new ModelBuilder();

	DirectionalShadowLight sl;
	CullingModelBatch sb;
	/**
	 * @param stateManager
	 */
	public GameState(StateManager stateManager) {
		super(stateManager);
		
		Bullet.init();
		p = new PhysicPlayer(0, 0.01f, -3, 100, 0, 0, 0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		sphere = mb.createSphere(1f, 1f, 1f, 16, 16, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Normal | Usage.Position);
		collisionShapes.add(new btCollisionObject());
		plane = mb.createBox(3f, 0.1f, 3f, new Material(ColorAttribute.createSpecular(Color.WHITE)), Usage.Normal | Usage.Position);
		instances.add(new Entity(sphere, 0, 2f, 0));
		instances.add(new Entity(
				mb.createRect(20f, 0f, 20f, 20f, 0f, -20f, -20f, 0f, -20f, -20f, 0f, 20f, 0f, 1f, 0f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Normal | Usage.Position), 0, 0,
				0));
		// instances.add(new Entity(plane));
		/*
		 * for(int x=-5;x<5;x++) { for(int y=-5;y<5;y++) { instances.add(new
		 * Entity(plane, x*3f, Math.abs(x*x+y*y)*0.1f, y*3f)); } }
		 */
		for (int i = 0; i < 15; i++) {
			instances.add(new Entity(mb.createBox(2.5f, 2.5f, 2.5f, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), Usage.Normal | Usage.Position), (float) (Math.random() * 2 - 1) * 20f,
					1.25f, (float) (Math.random() * 2 - 1) * 20f));
		}
		models = new CullingModelBatch();
		world = new Environment();
		world.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1));
		world.add((sl = new DirectionalShadowLight((int) (Gdx.graphics.getWidth() * 1.2f), (int) (Gdx.graphics.getHeight() * 1.2f), Gdx.graphics.getWidth() / 100, Gdx.graphics.getHeight() / 100, 0f,
				100f)).set(1f, 1f, 1f, new Vector3(-1f, -1f, -1f)));
		world.shadowMap = sl;

		coords = mb.createXYZCoordinates(1f, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Normal | Usage.Position);
		instances.add(new Entity(coords, 0, 1f, 0));

		sb = new CullingModelBatch(new DepthShaderProvider());
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dcprograming.game.states.State#render()
	 */
	@Override
	public void render() {
		// TODO Auto-generated method stub
		if (Gdx.input.isTouched()) {
			Gdx.input.setCursorCatched(true);
		}

		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.input.setCursorCatched(false);
		}

		float dtime = Gdx.graphics.getDeltaTime();

		sl.begin(p.playerPosition, p.playerCam.direction);
		sb.begin(sl.getCamera());
		sb.render(instances, world);
		sb.end();
		sl.end();

		Gdx.gl.glClearColor(0.2f, 0.7f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		instances.get(0).transform.translate(0, y, 0);
		if (instances.get(0).transform.getTranslation(new Vector3()).y <= 0.5f) {
			y = -y * 1f;
			y -= 9.81 * (dtime / 20);
			instances.get(0).transform.setTranslation(new Vector3(0, 0.5f, 0));
		} else {
			y -= 9.81 * (dtime / 20);
		}

		p.update(dtime, 9.81f);

		if (Gdx.input.isCursorCatched()) {
			p.rotate(3 * dtime);
			float cx = (Gdx.input.isKeyPressed(Keys.A) ? 2 : 0) + (Gdx.input.isKeyPressed(Keys.D) ? -2 : 0);
			float cy = (Gdx.input.isKeyPressed(Keys.SPACE) ? 2 : 0);
			float cz = (Gdx.input.isKeyPressed(Keys.W) ? 2 : 0) + (Gdx.input.isKeyPressed(Keys.S) ? -2 : 0);
			p.localTranslate(cx, cy, cz, dtime, instances);
		}
		models.begin(p.playerCam);
		models.render(instances, world);
		models.end();
		// p.logInfo()
		// models.render(instances.get(0), world);
		// models.render(instances.get(1), world);
		// for (int i = 2; i < instances.size(); i++)
		// if (instances.get(i).isVisible(p.playerCam)) {
		// models.render(instances.get(i), world);
		// System.out.println("Seen");
		// }
		models.end();
		System.out.println(Gdx.graphics.getFramesPerSecond());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dcprograming.game.states.State#update(float)
	 */
	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dcprograming.game.states.State#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
