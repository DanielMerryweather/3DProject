
package com.dcprograming.game.managers;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Creates a revised textbutton with manual sizing
 * 
 * @author Colton Giesbrecht
 * @dateCreated June 4, 2018
 * @dateCompleted June 4, 2018
 * @version 1.00
 */
public class SizedTextButton extends TextButton {

	private float prefWidth, prefHeight;

	/**
	 * Creates a textbutton with text and a style, and sets the width and height
	 * 
	 * @param text - the starting caption
	 * @param style - the style to use
	 * @param prefWidth - the set width
	 * @param prefHeight - the set height
	 */
	public SizedTextButton(String text, TextButtonStyle style, float prefWidth, float prefHeight) {
		super(text, style);
		this.prefHeight = prefHeight;
		this.prefWidth = prefWidth;
	}

	/**
	 * @return prefWidth
	 */
	@Override
	public float getPrefWidth() {
		return prefWidth;
	}

	/**
	 * @return prefHeight
	 */
	@Override
	public float getPrefHeight() {
		return prefHeight;
	}
}
