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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.dcprograming.game.managers.StateManager;

public class MenuState extends State {

	private TextButton hostButton;
	private TextButton joinButton;
	private Stage stage;

	/**
	 * @param stateManager
	 */
	public MenuState(StateManager stateManager) {
		super(stateManager);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		Skin buttonSkin = new Skin(new TextureAtlas(Gdx.files.internal("Button.atlas")));
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("YellowSwamp.ttf"));
		FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
		fontParameter.color = Color.YELLOW;
		fontParameter.size = 100;
		BitmapFont buttonFont = fontGen.generateFont(fontParameter);
		TextButtonStyle buttonStyle = new TextButtonStyle(buttonSkin.getDrawable("ButtonUp"), buttonSkin.getDrawable("ButtonCheck"), buttonSkin.getDrawable("ButtonCheck"), buttonFont);
		fontGen.dispose();
		hostButton = new TextButton("Host Game", buttonStyle);
		stage.addActor(hostButton);
		hostButton.align(Align.center);
		hostButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stateManager.setState(StateManager.GAME);

			}
		});
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
