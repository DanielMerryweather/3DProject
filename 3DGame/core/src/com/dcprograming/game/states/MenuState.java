/**
 * @author Colton Giesbrecht
 * @dateCreated May 24, 2018
 * @dateCompleted NOT COMPLETED
 * @version 1.00
 */
package com.dcprograming.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.dcprograming.game.managers.SizedTextField;
import com.dcprograming.game.managers.StateManager;

public class MenuState extends State {

	private TextButton hostButton;
	private TextButton joinButton;
	private TextButton quitButton;
	private TextButton statsButton;
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
		fontParameter.size = 100;
		BitmapFont buttonFont = fontGen.generateFont(fontParameter);
		TextButtonStyle buttonStyle = new TextButtonStyle(buttonSkin.getDrawable("BroomButtonReg"), buttonSkin.getDrawable("BroomButtonReg"), buttonSkin.getDrawable("BroomButtonReg"), buttonFont);
		buttonStyle.over = buttonSkin.getDrawable("BroomButtonHover");
		buttonStyle.overFontColor = Color.WHITE;
		buttonStyle.fontColor = Color.YELLOW;
		fontGen.dispose();
		hostButton = new TextButton("Host Game", buttonStyle);
		hostButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stateManager.setState(new NetworkTestingState(stateManager, "127.0.0.1"));
			}
		});
		joinButton = new TextButton("Join Game", buttonStyle);
		joinButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stateManager.setState(new NetworkTestingState(stateManager, ipTextField.getText()));
			}
		});
		statsButton = new TextButton("Stats", buttonStyle);
		quitButton = new TextButton("Quit", buttonStyle);
		quitButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {

				Gdx.app.exit();
			}
		});

		Skin textFieldSkin = new Skin(new TextureAtlas("TextFieldTextures.atlas"));
		TextFieldStyle textFieldStyle = new TextFieldStyle(buttonFont, Color.YELLOW, textFieldSkin.getDrawable("Cursor"), textFieldSkin.getDrawable("TextField"),
				textFieldSkin.getDrawable("TextField"));
		ipTextField = new SizedTextField("", textFieldStyle, 548);
		ipTextField.setMessageText("IP Address:");
		ipTextField.setAlignment(Align.center);
		joinGameGroup = new HorizontalGroup();
		joinGameGroup.space(10);
		joinGameGroup.addActor(ipTextField);
		joinGameGroup.addActor(joinButton);
		buttonTable = new Table();
		buttonTable.setFillParent(true);
		buttonTable.add(hostButton).pad(10).center().row();
		buttonTable.add(joinGameGroup).pad(10).center().row();
		buttonTable.add(statsButton).pad(10).center().row();
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
		System.out.println("Hi");
	}

}
