/**
 * Starts a new quidditch game as a host or as a client. If a host, it operates the ball, goal, and
 * score mechanics. Both clients and hosts use their network connection to render any host side
 * physics.
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted June 6, 2018
 * @version 3.55
 */
package com.dcprograming.game.states;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.dcprograming.game.core.Core;
import com.dcprograming.game.entities.Ball;
import com.dcprograming.game.entities.Entity;
import com.dcprograming.game.entities.GoalPost;
import com.dcprograming.game.entities.Player;
import com.dcprograming.game.entities.PlayerModel;
import com.dcprograming.game.entities.Wall;
import com.dcprograming.game.managers.CullingModelBatch;
import com.dcprograming.game.managers.StateManager;
import com.dcprogramming.game.networking.Client;
import com.dcprogramming.game.networking.Packet;
import com.dcprogramming.game.networking.Server;

@SuppressWarnings("deprecation")
public class NetworkGameState extends State {

	private static final float ARENA_WIDTH = 10, ARENA_DEPTH = 30, ARENA_HEIGHT = 5;

	Ball ball;
	int blueScore = 0;
	Label blueScoreLabel;
	LabelStyle blueScoreStyle;
	Client c;
	String desiredAddress = "127.0.0.1";
	ArrayList<Entity> entities = new ArrayList<Entity>();
	GoalPost goal1, goal2;
	float holdingPitch = 0, holdingYaw = 0;
	// PlayerModel holdingPlayer;
	String holdingPlayer = "";

	boolean isHeld = false;
	boolean isHost = false;
	boolean launch;
	ModelBuilder mb = new ModelBuilder();
	Player p;

	PlayerModel playerModel;
	int redScore = 0;
	Label redScoreLabel;

	LabelStyle redScoreStyle;
	CullingModelBatch renderer;
	Server s;

	CullingModelBatch sb;

	Ball sball;

	boolean scoreChange = false;

	DirectionalShadowLight sl;

	Stage stage;
	Label teamLabel;

	Environment world;

	/**
	 * Sets up server connection, player model, gui, and world map. If isHost is true, sets up the
	 * ball and starts the server.
	 * 
	 * @param stateManager - its managing state manager
	 */
	@SuppressWarnings("deprecation")
	public NetworkGameState(StateManager stateManager, String address, boolean isHost) {

		super(stateManager);
		desiredAddress = address;
		this.isHost = isHost;

		renderer = new CullingModelBatch();

		System.out.println(address);
		if (isHost) (s = new Server()).start();
		c = new Client(desiredAddress);
		p = new Player(-10, 0, 0, 110, 0, 0, 1);
		playerModel = new PlayerModel(0, -10, 0, Color.RED, mb);

		while (!c.pm.data.containsKey(System.getProperty("user.name")))
			c.serverUpdateRequest();

		c.pm.data.get(System.getProperty("user.name")).forEach(p -> {
			if (p.getIdentifier().equals("TEAM")) resetPlayerPos(p.getData().equals("RED"));
		});
		c.sendPacket(new Packet("X:" + p.playerPosition.x));
		c.sendPacket(new Packet("Y:" + p.playerPosition.y));
		c.sendPacket(new Packet("Z:" + p.playerPosition.z));

		c.sendPacket(new Packet("PITCH:" + p.pitch));
		c.sendPacket(new Packet("YAW:" + p.yaw));
		c.sendPacket(new Packet("LAUNCH:" + Gdx.input.isTouched()));

		if (isHost) {
			ball = new Ball(0, 0, 0);
			c.sendPacket(new Packet("BallX:" + ball.x));
			c.sendPacket(new Packet("BallY:" + ball.y));
			c.sendPacket(new Packet("BallZ:" + ball.z));
			c.sendPacket(new Packet("BallColour:" + ball.colour));
			c.sendPacket(new Packet("RedScore:" + redScore));
			c.sendPacket(new Packet("BlueScore:" + blueScore));
		}
		world = new Environment();
		world.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1));
		world.add((sl = new DirectionalShadowLight((int) (Gdx.graphics.getWidth() * 1.2f), (int) (Gdx.graphics.getHeight() * 1.2f), Gdx.graphics.getWidth() / 100, Gdx.graphics.getHeight() / 100, 0f, 100f)).set(1f, 1f, 1f, new Vector3(-1f, -1f, -1f)));
		world.shadowMap = sl;
		entities.add(new Wall(-ARENA_WIDTH / 2, -0.2f, -ARENA_DEPTH / 2, ARENA_WIDTH, 0.2f, ARENA_DEPTH, Color.GREEN));
		sb = new CullingModelBatch(new DepthShaderProvider());
		goal1 = new GoalPost(0, 2, -ARENA_DEPTH / 4, Color.CYAN);
		goal2 = new GoalPost(0, 2, ARENA_DEPTH / 4, Color.CORAL);
		goal1.update(0);
		goal2.update(0);
		entities.add(goal1);
		entities.add(goal2);
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("YellowSwamp.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = (int) (Core.pixels * 0.8f);
		BitmapFont scoreFont = fontGen.generateFont(parameter);
		redScoreStyle = new LabelStyle(scoreFont, Color.RED);
		blueScoreStyle = new LabelStyle(scoreFont, Color.BLUE);
		teamLabel = new Label("Blue Team", blueScoreStyle);
		redScoreLabel = new Label(String.valueOf(redScore), redScoreStyle);
		blueScoreLabel = new Label(String.valueOf(blueScore), blueScoreStyle);

		stage = new Stage();
		HorizontalGroup scores = new HorizontalGroup();
		scores.space(Core.pixels);
		scores.addActor(blueScoreLabel);
		scores.addActor(redScoreLabel);
		Container<HorizontalGroup> scoreContainer = new Container<HorizontalGroup>(scores).pad(Core.pixels / 2).top();
		stage.addActor(scoreContainer);
		scoreContainer.setFillParent(true);
		Container<Label> teamContainer = new Container<Label>(teamLabel).align(Align.bottomRight).pad(Core.pixels / 2);
		stage.addActor(teamContainer);
		teamContainer.setFillParent(true);
	}

	/**
	 * Disposes of resources
	 */
	@Override
	public void dispose() {

		c.disconnect();
		c = null;
		if (s != null) {
			s.stop();
		}
		try {
			s.servSocket.close();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s = null;
		System.gc();
	}

	/**
	 * Renders all client and server based entities. Also gathers the network packets for server
	 * reliant rendering.
	 */
	@Override
	public void render() {

		sl.begin(p.playerPosition, p.playerCam.direction);
		sb.begin(sl.getCamera());
		entities.forEach(e -> e.render(renderer, world));
		sb.render(playerModel.model, world);
		if (sball != null) {
			sball.render(sb, world);
		}
		sb.end();
		sl.end();

		Gdx.gl.glClearColor(0.2f, 0.7f, 1f, 1);

		renderer.begin(p.playerCam);
		entities.forEach(e -> e.render(renderer, world));
		c.serverUpdateRequest();
		PlayerModel otherPlayer;
		try {
			for (String plyr : c.pm.data.keySet()) {
				Color teamColour = Color.GRAY;
				Color ballColour = Color.GRAY;
				String teamColourString = "GREY";
				float x = 0;
				float y = -10;
				float z = 0;
				float pitch = 0;
				float yaw = 0;
				float ballx = 0f, bally = 0f, ballz = 0f;

				for (Packet p : c.pm.data.get(plyr)) {
					if (p.getIdentifier().equals("X")) {
						x = Float.parseFloat(p.getData());
					}
					else if (p.getIdentifier().equals("Y")) {
						y = Float.parseFloat(p.getData());
					}
					else if (p.getIdentifier().equals("Z")) {
						z = Float.parseFloat(p.getData());
					}
					else if (p.getIdentifier().equals("PITCH")) {
						pitch = Float.parseFloat(p.getData());
					}
					else if (p.getIdentifier().equals("YAW")) {
						yaw = Float.parseFloat(p.getData());
					}
					else if (p.getIdentifier().equals("TEAM")) {
						teamColourString = p.getData();
						teamColour = teamColourString.equals("RED") ? Color.RED : Color.BLUE;
					}
					else if (p.getIdentifier().equals("BallX"))
						ballx = Float.parseFloat(p.getData());
					else if (p.getIdentifier().equals("BallY"))
						bally = Float.parseFloat(p.getData());
					else if (p.getIdentifier().equals("BallZ")) {
						ballz = Float.parseFloat(p.getData());
						sball = new Ball(ballx, bally, ballz);
						sball.render(renderer, world);
					}
					else if (p.getIdentifier().equals("LAUNCH") && holdingPlayer.equals(plyr))
						launch = Boolean.parseBoolean(p.getData());
					else if (p.getIdentifier().equals("BallColour"))
						ballColour = p.getData().equals("BLUE") ? Color.BLUE : p.getData().equals("RED") ? Color.RED : Color.GRAY;
					else if (p.getIdentifier().equals("HoldingPlayer"))
						isHeld = p.getData().equals(System.getProperty("user.name"));
					else if (p.getIdentifier().equals("BlueScore") && !p.getData().equals(blueScoreLabel.getText().toString())) {
						blueScoreLabel.setText(p.getData());
						scoreChange = true;
					}
					else if (p.getIdentifier().equals("RedScore") && !p.getData().equals(redScoreLabel.getText().toString())) {
						redScoreLabel.setText(p.getData());
						scoreChange = true;
					}
				}
				otherPlayer = new PlayerModel(0, -10, 0, teamColour, mb);
				otherPlayer.model.transform.set(new Vector3(x, y + 1, z), new Quaternion().setEulerAnglesRad(-yaw / 180f * (float) Math.PI, -pitch / 180f * (float) Math.PI, 0));
				otherPlayer.x = x;
				otherPlayer.y = y;
				otherPlayer.z = z;
				otherPlayer.update(Gdx.graphics.getDeltaTime());
				if (!plyr.equals(System.getProperty("user.name"))) {
					otherPlayer.render(renderer, world);
				}
				else {
					teamLabel.setStyle(teamColourString.equals("RED") ? redScoreStyle : blueScoreStyle);
					teamLabel.setText(teamColourString.equals("RED") ? "Red Team" : "Blue Team");
					if (scoreChange) {
						resetPlayerPos(teamColourString.equals("RED"));
						scoreChange = false;
					}
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
				if (sball != null) sball.changeColor(ballColour);
			}
		}
		catch (Exception e) {

		}
		renderer.end();
		stage.act();
		stage.draw();
	}

	/**
	 * Updates the world, ball, player position, goal, and checks collisions.
	 */
	@Override
	public void update(float deltaTime) {

		if (ball != null) {
			if (!holdingPlayer.equals("") && !c.pm.data.keySet().contains(holdingPlayer)) {
				holdingPlayer = "";
				ball.colour = "GREY";
			}

			if (holdingPlayer.equals("") && (ball.intercept(goal1) && ball.colour.equals("RED") || ball.intercept(goal2) && ball.colour.equals("BLUE"))) {

				ball.reflectDir(0, 0, 0);
				ball.x = 0;
				ball.y = 0;
				ball.z = 0;
				if (ball.colour.equals("RED"))
					redScore++;
				else blueScore++;
				// System.out.println(redScore + " " + blueScore);
				ball.colour = "GREY";
			}
			ball.update(deltaTime, holdingPitch, holdingYaw, launch, !holdingPlayer.equals(""));
			ball.model.transform.setTranslation(ball.x, ball.y, ball.z);
			if (launch) {
				holdingPlayer = "";
				launch = false;

			}
			if (ball.x < -ARENA_WIDTH / 2) {
				ball.x = -ARENA_WIDTH / 2;
				ball.reflectDir(-0.8F, 0.8F, 0.8F);
			}
			else if (ball.x > ARENA_WIDTH / 2) {
				ball.x = ARENA_WIDTH / 2;
				ball.reflectDir(-0.8F, 0.8F, 0.8F);
			}
			if (ball.y < 0) {
				ball.y = 0;
				ball.reflectDir(0.8F, -0.8F, 0.8F);
			}
			else if (ball.y > ARENA_HEIGHT) {
				ball.y = ARENA_HEIGHT;
				ball.reflectDir(0.8F, -0.8F, 0.8F);
			}
			if (ball.z < -ARENA_DEPTH / 2) {
				ball.reflectDir(0.8F, 0.8F, -0.8F);
				ball.z = -ARENA_DEPTH / 2;
			}
			else if (ball.z > ARENA_DEPTH / 2) {
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
			c.sendPacket(new Packet("HoldingPlayer:" + (holdingPlayer.equals("") ? "NONE" : holdingPlayer)));
			c.sendPacket(new Packet("RedScore:" + redScore));
			c.sendPacket(new Packet("BlueScore:" + blueScore));
		}

		if (Gdx.input.isTouched()) {
			Gdx.input.setCursorCatched(true);
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (!Gdx.input.isCursorCatched())

				stateManager.setState(StateManager.MENU);
			else Gdx.input.setCursorCatched(false);
		}

		if (Gdx.input.isCursorCatched()) {
			p.rotate(0.3f);
			if (!isHeld) {
				float cx = (Gdx.input.isKeyPressed(Keys.A) ? 4 : 0) + (Gdx.input.isKeyPressed(Keys.D) ? -4 : 0);
				float cy = (Gdx.input.isKeyPressed(Keys.SPACE) ? 4 : 0) + (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) ? -4 : 0);
				float cz = (Gdx.input.isKeyPressed(Keys.W) ? 4 : 0) + (Gdx.input.isKeyPressed(Keys.S) ? -4 : 0);
				p.localTranslate(cx, cy, cz, deltaTime);
			}
		}

		if (p.playerPosition.x < -ARENA_WIDTH / 2)
			p.setPosition(-ARENA_WIDTH / 2 - p.playerPosition.x, 0, 0);
		else if (p.playerPosition.x > ARENA_WIDTH / 2) p.setPosition(ARENA_WIDTH / 2 - p.playerPosition.x, 0, 0);
		if (p.playerPosition.y < 0)
			p.setPosition(0, -p.playerPosition.y, 0);
		else if (p.playerPosition.y > ARENA_HEIGHT) p.setPosition(0, ARENA_HEIGHT - p.playerPosition.y, 0);
		if (p.playerPosition.z < -ARENA_DEPTH / 2)
			p.setPosition(0, 0, -ARENA_DEPTH / 2 - p.playerPosition.z);
		else if (p.playerPosition.z > ARENA_DEPTH / 2) p.setPosition(0, 0, ARENA_DEPTH / 2 - p.playerPosition.z);

		playerModel.model.transform.set(p.playerPosition.cpy().add(new Vector3(0, 1, 0)), new Quaternion().setEulerAngles(-p.playerCam.view.getRotation(new Quaternion()).getYaw(), -p.playerCam.view.getRotation(new Quaternion()).getPitch(), 0));
		launch = false;
	}

	/**
	 * Sets the client player to their default team-based spawn position.
	 * 
	 * @param isRed - if player is on the red team
	 */
	private void resetPlayerPos(boolean isRed) {

		if (isRed) {
			p.playerPosition.set(0, 0, ARENA_DEPTH / 4);
		}
		else {
			p.playerPosition.set(0, 0, -ARENA_DEPTH / 4);
		}
		p.playerCam.position.set(p.playerPosition.x, p.playerPosition.y + 0.75f, p.playerPosition.z);
		p.playerCam.update();
	}

}
