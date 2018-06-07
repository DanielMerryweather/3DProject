/**
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.states;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.dcprograming.game.core.Core;
import com.dcprograming.game.managers.SizedTextButton;
import com.dcprograming.game.managers.SizedTextField;
import com.dcprograming.game.managers.StateManager;

public class MenuState extends State {

	private SizedTextButton hostButton;
	private SizedTextButton joinButton;
	private SizedTextButton quitButton;
	private Image title;
	private TextField ipTextField;
	private Stage stage;
	private Table buttonTable;
	private HorizontalGroup joinGameGroup;

	/**
	 * @param stateManager
	 */
	public MenuState(final StateManager stateManager) {
		super(stateManager);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		Skin buttonSkin = new Skin(new TextureAtlas("ButtonTextures2.atlas"));
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("YellowSwamp.ttf"));
		FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
		// fontParameter.color = Color.YELLOW;
		fontParameter.size = (int) (Core.pixels / 2);
		BitmapFont buttonFont = fontGen.generateFont(fontParameter);
		TextButtonStyle buttonStyle = new TextButtonStyle(buttonSkin.getDrawable("BroomButtonReg"), buttonSkin.getDrawable("BroomButtonReg"), buttonSkin.getDrawable("BroomButtonReg"), buttonFont);
		buttonStyle.over = buttonSkin.getDrawable("BroomButtonHover");
		buttonStyle.overFontColor = Color.WHITE;
		buttonStyle.fontColor = Color.YELLOW;
		fontGen.dispose();
		hostButton = new SizedTextButton("Host Game", buttonStyle, Core.pixels * 4, Core.pixels);
		hostButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {

				stateManager.setState(new NetworkGameState(stateManager, "127.0.0.1", true));
			}
		});
		joinButton = new SizedTextButton("Join Game", buttonStyle, Core.pixels * 4, Core.pixels);
		joinButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				boolean valid = false;
				try {
					InetAddress i = InetAddress.getByName(ipTextField.getText());
					valid = i.isReachable(100);
				} catch (Exception e) {}
				if(valid) {
					stateManager.setState(new NetworkGameState(stateManager, ipTextField.getText(), false));
				}
			}
		});

		quitButton = new SizedTextButton("Quit", buttonStyle, Core.pixels * 4, Core.pixels);
		quitButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {

				Gdx.app.exit();
			}
		});

		Skin textFieldSkin = new Skin(new TextureAtlas("TextFieldTextures.atlas"));
		TextFieldStyle textFieldStyle = new TextFieldStyle(buttonFont, Color.YELLOW, textFieldSkin.getDrawable("Cursor"), textFieldSkin.getDrawable("TextField"),
				textFieldSkin.getDrawable("TextField"));
		ipTextField = new SizedTextField("", textFieldStyle, Core.pixels * 4, Core.pixels);
		ipTextField.setMessageText("IP Address:");
		ipTextField.setAlignment(Align.center);
		joinGameGroup = new HorizontalGroup();
		joinGameGroup.space(10);
		joinGameGroup.addActor(ipTextField);
		joinGameGroup.addActor(joinButton);
		title = new Image(new Texture(Gdx.files.internal("Title.png")));
		buttonTable = new Table();
		buttonTable.setFillParent(true);
		buttonTable.add(title).size(Core.pixels * 8, Core.pixels * 3).pad(40).center().row();
		buttonTable.add(hostButton).pad(10).center().row();
		buttonTable.add(joinGameGroup).pad(10).center().row();
		buttonTable.add(quitButton).pad(10).center().row();
		stage.addActor(buttonTable);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		stage.act();
		stage.draw();
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
	}

}
