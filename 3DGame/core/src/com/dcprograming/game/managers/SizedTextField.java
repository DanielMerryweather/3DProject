/**
 * A textfield with the added ability to set the size
 * @author Colton Giesbrecht
 * @dateCreated May 25, 2018
 * @dateCompleted May 25, 2018
 * @version 1.00
 */
package com.dcprograming.game.managers;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class SizedTextField extends TextField {

	private float prefWidth, prefHeight;

	/**
	 * Creates a textfield with text and a style, and sets the width and height
	 * 
	 * @param text - the starting caption
	 * @param style - the style to use
	 * @param prefWidth - the set width
	 * @param prefHeight - the set height
	 */
	public SizedTextField(String text, TextFieldStyle style, float prefWidth, float prefHeight) {
		super(text, style);
		this.prefWidth = prefWidth;
		this.prefHeight = prefHeight;
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
