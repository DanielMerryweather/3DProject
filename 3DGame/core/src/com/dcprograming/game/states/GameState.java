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
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.dcprograming.game.entities.Ball;
import com.dcprograming.game.entities.Entity;
import com.dcprograming.game.entities.PhysicPlayer;
import com.dcprograming.game.entities.Room;
import com.dcprograming.game.managers.CullingModelBatch;
import com.dcprograming.game.managers.StateManager;

public class GameState extends State {

	private Ball ball;
	private Room room;
	private ModelInstance box;
	private CullingModelBatch renderer;

	private Environment world;
	private PhysicPlayer player;
	private ArrayList<PhysicPlayer> players;
	private ArrayList<Entity> entities;

	/**
	 * @param stateManager
	 */
	public GameState(final StateManager stateManager) {

		super(stateManager);
		players = new ArrayList<PhysicPlayer>();
		player = new PhysicPlayer(0, 0, -4, 100, 0, 0, 0, Color.BLUE);
		players.add(player);
		ball = new Ball(0, 0, 0);
		renderer = new CullingModelBatch();

		world = new Environment();
		world.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 0.6f));
		world.add(new PointLight().set(Color.WHITE, -2, 3, -5, 30));
		entities = new ArrayList<Entity>();
		entities.add(ball);
		room = new Room(0, 0, 0, 30, 10, 60, Color.RED, Color.WHITE, true);
		entities.add(room);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dcprograming.game.states.State#render()
	 */
	@Override
	public void render() {

		player.playerCam.update();
		renderer.begin(player.playerCam);
		entities.forEach(e -> e.render(renderer, world));
		player.render(renderer, world);
		renderer.end();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dcprograming.game.states.State#update(float)
	 */
	@Override
	public void update(float deltaTime) {

		if (Gdx.input.isTouched())
			Gdx.input.setCursorCatched(true);
		else if (Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.input.setCursorCatched(false);

		player.update(deltaTime, 9.81f);
		entities.forEach(e -> e.update(deltaTime));

		if (Gdx.input.isCursorCatched()) {
			player.rotate(3 * deltaTime);
			float dz = 0, dx = 0, dy = 0;
			if (Gdx.input.justTouched())
				ball.update(deltaTime, players, true, room);
			else
				ball.update(deltaTime, players, false, room);
			if (Gdx.input.isKeyPressed(Keys.W))
				dz = 5;
			if (Gdx.input.isKeyPressed(Keys.S))
				dz = -5;
			if (Gdx.input.isKeyPressed(Keys.D))
				dx = -5;
			if (Gdx.input.isKeyPressed(Keys.A))
				dx = 5;
			if (Gdx.input.isKeyPressed(Keys.SPACE))
				dy = 5;
			if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				dy = -5;
			if (Gdx.input.isKeyJustPressed(Keys.R)) {

				ball.changeColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1));
			}
			player.localTranslate(dx, dy, dz, deltaTime, entities);

		}
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
