/**
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.states;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.dcprograming.game.entities.Entity;
import com.dcprograming.game.entities.PhysicPlayer;
import com.dcprograming.game.entities.PlayerModel;
import com.dcprograming.game.managers.CullingModelBatch;
import com.dcprograming.game.managers.StateManager;
import com.dcprogramming.game.networking.Client;
import com.dcprogramming.game.networking.Packet;
import com.dcprogramming.game.networking.Server;

public class NetworkTestingState extends State {

	CullingModelBatch renderer;
	ArrayList<Entity> entities = new ArrayList<Entity>();
	ModelBuilder mb = new ModelBuilder();
	Environment world;

	PlayerModel playerModel;

	DirectionalShadowLight sl;
	CullingModelBatch sb;

	PhysicPlayer p;

	String desiredAddress = "127.0.0.1";

	Client c;
	Server s;

	/**
	 * @param stateManager
	 */
	public NetworkTestingState(StateManager stateManager, String address, boolean isHost) {
		super(stateManager);

		if (isHost) {
			s = new Server();
			try {
				s.runServer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		renderer = new CullingModelBatch();
		p = new PhysicPlayer(0, 0, 0, 110, 0, 0, 1);

		desiredAddress = address;
		System.out.println(address);
		c = new Client(desiredAddress);
		c.sendPacket(new Packet("X:" + p.playerPosition.x));
		c.sendPacket(new Packet("Y:" + p.playerPosition.y));
		c.sendPacket(new Packet("Z:" + p.playerPosition.z));
		c.sendPacket(new Packet("PITCH:" + p.pitch));
		c.sendPacket(new Packet("YAW:" + p.yaw));

		world = new Environment();
		world.add((sl = new DirectionalShadowLight((int) (Gdx.graphics.getWidth() * 1.2f), (int) (Gdx.graphics.getHeight() * 1.2f), Gdx.graphics.getWidth() / 100, Gdx.graphics.getHeight() / 100, 0f,
				100f)).set(1f, 1f, 1f, new Vector3(-1f, -1f, -1f)));
		world.shadowMap = sl;
		entities.add(new Entity(mb.createBox(10, 2, 10, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Normal | Usage.Position), 0, -2, 0));
		sb = new CullingModelBatch(new DepthShaderProvider());

		playerModel = new PlayerModel(Color.RED, mb);
	}

	@Override
	public void render() {

		sl.begin(p.playerPosition, p.playerCam.direction);
		sb.begin(sl.getCamera());
		sb.render(entities, world);
		sb.render(playerModel.model, world); // Render players shadow but don't actually show the personal player model
		sb.end();
		sl.end();

		Gdx.gl.glClearColor(0.2f, 0.7f, 1f, 1);

		renderer.begin(p.playerCam);
		renderer.render(entities, world);
		for (String plyr : Client.pm.data.keySet()) {
			PlayerModel otherPlayer = new PlayerModel(Color.BLUE, mb);
			float x = 0;
			float y = 0;
			float z = 0;
			float pitch = 0;
			float yaw = 0;
			for (Packet p : Client.pm.data.get(plyr)) {
				if (p.getIdentifier().equals("X")) {
					x = Float.parseFloat(p.getData());
				} else if (p.getIdentifier().equals("Y")) {
					y = Float.parseFloat(p.getData());
				} else if (p.getIdentifier().equals("Z")) {
					z = Float.parseFloat(p.getData());
				} else if (p.getIdentifier().equals("PITCH")) {
					pitch = Float.parseFloat(p.getData());
				} else if (p.getIdentifier().equals("YAW")) {
					yaw = Float.parseFloat(p.getData());
				}
			}
			otherPlayer.model.transform.setFromEulerAnglesRad(-yaw / 180f * (float) Math.PI, -pitch / 180f * (float) Math.PI, 0);
			otherPlayer.model.transform.setTranslation(x, y, z);
			renderer.render(otherPlayer.model, world);
		}
		renderer.end();
	}

	@Override
	public void update(float deltaTime) {

		c.sendPacket(new Packet("X:" + p.playerPosition.x));
		c.sendPacket(new Packet("Y:" + p.playerPosition.y));
		c.sendPacket(new Packet("Z:" + p.playerPosition.z));
		c.sendPacket(new Packet("PITCH:" + p.pitch));
		c.sendPacket(new Packet("YAW:" + p.yaw));

		if (Gdx.input.isTouched()) {
			Gdx.input.setCursorCatched(true);
		}

		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.input.setCursorCatched(false);
		}

		if (Gdx.input.isCursorCatched()) {
			p.rotate(0.3f);

			float cx = (Gdx.input.isKeyPressed(Keys.A) ? 1 : 0) + (Gdx.input.isKeyPressed(Keys.D) ? -1 : 0);
			float cy = (Gdx.input.isKeyPressed(Keys.SPACE) ? 1 : 0) + (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) ? -1 : 0);
			float cz = (Gdx.input.isKeyPressed(Keys.W) ? 1 : 0) + (Gdx.input.isKeyPressed(Keys.S) ? -1 : 0);
			p.localTranslate(cx, cy, cz, deltaTime * 2, entities);
		}

		playerModel.model.transform.setToTranslation(p.playerPosition);

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		c.disconnect();
		if (s != null)
			try {
				s.closeServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
