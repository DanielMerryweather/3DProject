
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
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.dcprograming.game.entities.Ball;
import com.dcprograming.game.entities.Entity;
import com.dcprograming.game.entities.GoalPost;
import com.dcprograming.game.entities.PhysicPlayer;
import com.dcprograming.game.entities.PlayerModel;
import com.dcprograming.game.entities.Wall;
import com.dcprograming.game.managers.CullingModelBatch;
import com.dcprograming.game.managers.StateManager;
import com.dcprogramming.game.networking.Client;
import com.dcprogramming.game.networking.Packet;
import com.dcprogramming.game.networking.Server;

public class NetworkTestingState extends State {

	private static final float ARENA_WIDTH = 10, ARENA_DEPTH = 30, ARENA_HEIGHT = 5;

	Ball ball;
	GoalPost goal1, goal2;
	Client c;
	String desiredAddress = "127.0.0.1";
	ArrayList<Entity> entities = new ArrayList<Entity>();
	Label redScoreLabel;
	Label blueScoreLabel;

	// PlayerModel holdingPlayer;
	String holdingPlayer = "";
	float holdingPitch = 0, holdingYaw = 0;
	boolean launch;
	ModelBuilder mb = new ModelBuilder();

	PhysicPlayer p;
	PlayerModel playerModel;
	CullingModelBatch renderer;

	Server s;

	boolean isHost = false;
	
	CullingModelBatch sb;

	Ball sball;
	DirectionalShadowLight sl;

	Environment world;

	/**
	 * @param stateManager
	 */
	public NetworkTestingState(StateManager stateManager, String address, boolean isHost) {

		super(stateManager);
		desiredAddress = address;
		this.isHost = isHost;
		
		renderer = new CullingModelBatch();
		p = new PhysicPlayer(-10, 0, 0, 110, 0, 0, 1);

		System.out.println(address);
		if (isHost)
			(s = new Server()).start();
		c = new Client(desiredAddress);
		c.sendPacket(new Packet("X:" + p.playerPosition.x));
		c.sendPacket(new Packet("Y:" + p.playerPosition.y));
		c.sendPacket(new Packet("Z:" + p.playerPosition.z));
		c.sendPacket(new Packet("PITCH:" + p.pitch));
		c.sendPacket(new Packet("YAW:" + p.yaw));
		c.sendPacket(new Packet("LAUNCH:" + Gdx.input.isTouched()));
		playerModel = new PlayerModel(0, -10, 0, Color.RED, mb);
		if (isHost) {
			ball = new Ball(0, 0, 0);
			c.sendPacket(new Packet("BallX:" + ball.x));
			c.sendPacket(new Packet("BallY:" + ball.y));
			c.sendPacket(new Packet("BallZ:" + ball.z));
			c.sendPacket(new Packet("BallColour:" + ball.colour));
		}
		world = new Environment();
		world.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1));
		world.add((sl = new DirectionalShadowLight((int) (Gdx.graphics.getWidth() * 1.2f), (int) (Gdx.graphics.getHeight() * 1.2f), Gdx.graphics.getWidth() / 100, Gdx.graphics.getHeight() / 100, 0f,
				100f)).set(1f, 1f, 1f, new Vector3(-1f, -1f, -1f)));
		world.shadowMap = sl;
		entities.add(new Wall(-ARENA_WIDTH / 2, -0.2f, -ARENA_DEPTH / 2, ARENA_WIDTH, 0.2f, ARENA_DEPTH, Color.GREEN));
		// entities.add(new Entity(mb.createBox(10, 2, 10, new
		// Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Normal |
		// Usage.Position), 0, -2, 0));
		sb = new CullingModelBatch(new DepthShaderProvider());
		goal1 = new GoalPost(0, 0, -ARENA_DEPTH / 4);
		goal2 = new GoalPost(0, 0, ARENA_DEPTH / 4);
		goal1.update(0);
		goal2.update(0);
		entities.add(goal1);
		entities.add(goal2);

	}

	@Override
	public void dispose() {

		// TODO Auto-generated method stub
		c.disconnect();
		c = null;
		if(s != null) {
			s.stop();
		}
		s = null;
		System.gc();
	}

	@Override
	public void render() {

		sl.begin(p.playerPosition, p.playerCam.direction);
		sb.begin(sl.getCamera());
		entities.forEach(e -> e.render(renderer, world));
		sb.render(playerModel.model, world); // Render players shadow but don't actually show the
		if (sball != null) {
			sball.render(sb, world);
		}
		sb.end();
		sl.end();

		Gdx.gl.glClearColor(0.2f, 0.7f, 1f, 1);

		renderer.begin(p.playerCam);
		entities.forEach(e -> e.render(renderer, world));

		PlayerModel otherPlayer;
		for (String plyr : Client.pm.data.keySet()) {
			Color teamColour = Color.GRAY;
			Color ballColour = Color.GRAY;
			String teamColourString = "GREY";
			float x = 0;
			float y = 0;
			float z = 0;
			float pitch = 0;
			float yaw = 0;
			float ballx = 0f, bally = 0f, ballz = 0f;
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
				} else if (p.getIdentifier().equals("TEAM")) {
					teamColourString = p.getData();
					teamColour = teamColourString.equals("RED") ? Color.RED : Color.BLUE;
				} else if (p.getIdentifier().equals("BallX"))
					ballx = Float.parseFloat(p.getData());
				else if (p.getIdentifier().equals("BallY"))
					bally = Float.parseFloat(p.getData());
				else if (p.getIdentifier().equals("BallZ")) {
					ballz = Float.parseFloat(p.getData());
					sball = new Ball(ballx, bally, ballz);
					sball.render(renderer, world);
				} else if (p.getIdentifier().equals("LAUNCH") && holdingPlayer.equals(plyr))
					launch = Boolean.parseBoolean(p.getData());
				else if (p.getIdentifier().equals("BallColour"))
					ballColour = p.getData().equals("BLUE") ? Color.BLUE : p.getData().equals("RED") ? Color.RED : Color.GRAY;
			}
			otherPlayer = new PlayerModel(0, -10, 0, teamColour, mb);
			otherPlayer.model.transform.set(new Vector3(x, y + 1, z), new Quaternion().setEulerAnglesRad(-yaw / 180f * (float) Math.PI, -pitch / 180f * (float) Math.PI, 0));
			// otherPlayer.model.transform.setTranslation();
			// otherPlayer.model.transform.setFromEulerAnglesRad(-yaw / 180f * (float)
			// Math.PI,
			// -pitch / 180f * (float) Math.PI, 0);
			otherPlayer.x = x;
			otherPlayer.y = y;
			otherPlayer.z = z;
			otherPlayer.update(Gdx.graphics.getDeltaTime());
			if (!plyr.equals(System.getProperty("user.name"))) {
				otherPlayer.render(renderer, world);
			}

			if (ball != null && holdingPlayer.equals("") && otherPlayer.intercept(ball)) {
				// System.out.println("Hi");
				holdingPlayer = plyr;
				ball.colour = teamColourString;
			}
			if (holdingPlayer.equals(plyr)) {
				holdingPitch = pitch;
				holdingYaw = yaw;
				this.ball.x = otherPlayer.x;
				this.ball.y = otherPlayer.y;
				this.ball.z = otherPlayer.z;
				// System.out.println(ball.x);
			}
			if (sball != null)
				sball.changeColor(ballColour);
		}

		// playerModel.render(renderer, world);
		renderer.end();

	}

	@Override
	public void update(float deltaTime) {

		// System.gc();

		if (ball != null) {
			if (!holdingPlayer.equals("") && !Client.pm.data.keySet().contains(holdingPlayer)) {
				holdingPlayer = "";
				ball.colour = "GREY";
			}

			if (holdingPlayer.equals("") && (ball.intercept(goal1) || ball.intercept(goal2))) {

				ball.reflectDir(0, 0, 0);
				ball.x = 0;
				ball.y = 0;
				ball.z = 0;
				ball.colour = "GREY";
			}
			ball.update(deltaTime, holdingPitch, holdingYaw, launch, !holdingPlayer.equals(""));
			ball.model.transform.setTranslation(ball.x, ball.y, ball.z);
			if (launch) {
				System.out.println("The Launch");
				// ball.x = 0;
				// ball.y = 0;
				// ball.z = 0;
				holdingPlayer = "";
				launch = false;

			}
			if (ball.x < -ARENA_WIDTH / 2) {
				ball.x = -ARENA_WIDTH / 2;
				ball.reflectDir(-0.8F, 0.8F, 0.8F);
			} else if (ball.x > ARENA_WIDTH / 2) {
				ball.x = ARENA_WIDTH / 2;
				ball.reflectDir(-0.8F, 0.8F, 0.8F);
			}
			if (ball.y < 0) {
				ball.y = 0;
				ball.reflectDir(0.8F, -0.8F, 0.8F);
			} else if (ball.y > ARENA_HEIGHT) {
				ball.y = ARENA_HEIGHT;
				ball.reflectDir(0.8F, -0.8F, 0.8F);
			}
			if (ball.z < -ARENA_DEPTH / 2) {
				ball.reflectDir(0.8F, 0.8F, -0.8F);
				ball.z = -ARENA_DEPTH / 2;
			} else if (ball.z > ARENA_DEPTH / 2) {
				ball.reflectDir(0.8F, 0.8F, -0.8F);
				ball.z = ARENA_DEPTH / 2;
			}
		}

		c.sendPacket(new Packet("X:" + p.playerPosition.x));
		c.sendPacket(new Packet("Y:" + p.playerPosition.y));
		c.sendPacket(new Packet("Z:" + p.playerPosition.z));
		c.sendPacket(new Packet("PITCH:" + p.pitch));
		c.sendPacket(new Packet("YAW:" + p.yaw));
		c.sendPacket(new Packet("LAUNCH:" + Gdx.input.isTouched()));
		if (ball != null) {
			c.sendPacket(new Packet("BallX:" + ball.x));
			c.sendPacket(new Packet("BallY:" + ball.y));
			c.sendPacket(new Packet("BallZ:" + ball.z));
			c.sendPacket(new Packet("BallColour:" + ball.colour));
		}

		if (Gdx.input.isTouched()) {
			Gdx.input.setCursorCatched(true);
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (!Gdx.input.isCursorCatched())
				if (isHost)
					Gdx.app.exit();
				else
					stateManager.setState(StateManager.MENU);
			else
				Gdx.input.setCursorCatched(false);
		}

		if (Gdx.input.isCursorCatched()) {
			p.rotate(0.3f);

			float cx = (Gdx.input.isKeyPressed(Keys.A) ? 4 : 0) + (Gdx.input.isKeyPressed(Keys.D) ? -4 : 0);
			float cy = (Gdx.input.isKeyPressed(Keys.SPACE) ? 4 : 0) + (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) ? -4 : 0);
			float cz = (Gdx.input.isKeyPressed(Keys.W) ? 4 : 0) + (Gdx.input.isKeyPressed(Keys.S) ? -4 : 0);
			p.localTranslate(cx, cy, cz, deltaTime);
		}

		if (p.playerPosition.x < -ARENA_WIDTH / 2)
			p.setPosition(-ARENA_WIDTH / 2 - p.playerPosition.x, 0, 0);
		else if (p.playerPosition.x > ARENA_WIDTH / 2)
			p.setPosition(ARENA_WIDTH / 2 - p.playerPosition.x, 0, 0);
		if (p.playerPosition.y < 0)
			p.setPosition(0, -p.playerPosition.y, 0);
		else if (p.playerPosition.y > ARENA_HEIGHT)
			p.setPosition(0, ARENA_HEIGHT - p.playerPosition.y, 0);
		if (p.playerPosition.z < -ARENA_DEPTH / 2)
			p.setPosition(0, 0, -ARENA_DEPTH / 2 - p.playerPosition.z);
		else if (p.playerPosition.z > ARENA_DEPTH / 2)
			p.setPosition(0, 0, ARENA_DEPTH / 2 - p.playerPosition.z);

		// playerModel.model.transform.setFromEulerAngles(-p.playerCam.view.getRotation(new
		// Quaternion()).getYaw(), -p.playerCam.view.getRotation(new
		// Quaternion()).getPitch(), 0);
		playerModel.model.transform.set(p.playerPosition.cpy().add(new Vector3(0, 1, 0)),
				new Quaternion().setEulerAngles(-p.playerCam.view.getRotation(new Quaternion()).getYaw(), -p.playerCam.view.getRotation(new Quaternion()).getPitch(), 0));
		// playerModel.model.transform.setToTranslation(p.playerPosition);
		launch = false;
	}

}
